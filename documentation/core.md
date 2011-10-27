---
layout: core
---
# Cotopaxi Core

The minimum necessary you will need from framework is the Core. Even being the minimal part of the framework it still has a lots of interesting features. 

Core is responsible to route requests and delegate them to the correct controller’s action, rendering the view, managing dependency injection, setting up boot loaders and interceptors, and a logging system.

## Routing

Remember our config application file? Well, we are going to fill out the controllers section. The syntax is quite simple, first of all you need to specify the controllers that will be processing the incoming request:

<pre class="prettyprint">
controllers
    br.octahedron.ControllerOne
    (...)
    br.octahedron.ControllerTwo
    (...)
</pre>

After that, you need to specify the each URL that will be handled by routing system. It’s formed by three tokens: URI pattern, HTTP method and action name. Let’s see an example:

<pre class="prettyprint">
controllers
    br.octahedron.ControllerOne
        /users        GET       List
        /user/delete  DELETE    Destroy
</pre>

That’s all? Almost, you can also pass any parameter you want.

<pre class="prettyprint">
controllers
    br.octahedron.ControllerOne
        /users            GET     List
        /user/delete      DELETE  Destroy
        /user/show/{id}   GET     Show
</pre>

Notice that the third pattern will match request to /user/show/2 or /user/show/dd. You will need to validate the input on controller.
If there is any conflict like

<pre class="prettyprint">
controllers
    br.octahedron.ControllerOne
        /user/list    GET   List
        /user/{id}    GET   Show
</pre>

there will be no problem cause if any /user/list request comes, it will match the /user/list pattern. But if you declare

<pre class="prettyprint">
controllers
    br.octahedron.ControllerOne
        /user/{action}  GET   Action
        /user/{id}      GET   Show
</pre>

it will be impossible to decide which one of the patterns has to be chosen to match the URL request.

## Controllers

### Context request

Before start coding, it’s useful to explain how is the request context behaviour. We decided that every request needed to have its own context. What does that means? We are trying to be stateless, if we have, for example, two concurrent requests and you modify somehow the context of one (putting some value on output) of the requests, the other one stay intact.

Therefore, the request process becomes thread-safe and we take advantage of multi-thread feature of Java. Isn’t that cool?

### Declaring your controller class

Being more objective, after configuration of URL routing, it’s necessary to start coding (yay!). Once you specified the controller class br.octahedron.ControllerOne, you have to create that class and extends the Controller class provided by the framework.

<pre class="prettyprint">
package br.octahedron;

import br.octahdron.cotopaxi.controller.Controller;

public class ControllerOne extends Controller {
    (...)
}
</pre>

### Actions

Ok, but you have also to write the actions the will process the request when it comes. The framework uses the following rule: HTTP method + action name. So if you declared on your configuration file

<pre class="prettyprint">
controllers
    br.octahedron.ControllerOne
        /users            GET     List
        /user/create      PUT     Create
        /user/show/{id}   GET     Show
</pre>

you need to have

<pre class="prettyprint">
package br.octahedron;

import br.octahdron.cotopaxi.controller.Controller;

public class ControllerOne extends Controller {
    public void getList() { }
    public void putCreate() { }
    public void getShow() { }
}
</pre>

Notice that we didn’t passed any arguments to any method. The signature is just a public void method without any arguments. If you want to get the request parameters, you should use the methods inherited from the cotopaxi’s Controller class.

### Helper methods

Cotopaxi provides a lots of helper methods to increase the developer productivity while coding the controller’s actions. Let’s see them.

**InputController**

<pre class="prettyprint">
in(key) - recover any request parameter value
in(key, converter) - recover any request parameter value, and converts to a specific type
values(key) - recover any request parameter multiple value
session(key) - recover any data from session
header(key) - recover any request header parameter 
cookie(key) - recover any request cookies parameter
serverName() - gets the server name, Eg. www.octahedron.com.br
fullRequestedUrl() - gets the full requested URL, Eg. www.octahedron.com.br/contact
relativeRequestedUrl() - gets the relative requested URL, Eg. /contact
</pre>

Those are the common methods to manipulate the input parameters and attributes. That’s not all, folks. Where is the methods to set output values, render and redirect the responses? Here we go.

**Controller**

<pre class="prettyprint">
out(key, value) - puts a value associated with a variable on output to be accesible on view
echo() - echos all input parameters to out put, with same names; useful to handle form errors
session(key, value) - stores a value associated with a variable on session
cookie(key, value) - stores the given key/value pair using cookies
header(key, value) - adds the given key/value pair to response header
render(template, code) - renders given template with the given response code
success(template) - shortcut to render(template, 200)
error(template) - shortcut to render(template, 500)
invalid(template) - shortcut to render(template, 400)
forbidden(template) - shortcut to render(template, 403)
notFound(template) - shortcut to render(template, 404)
asJSON(code) - renders all output objects as json (Thanks [flexjson](http://flexjson.sourceforge.net/) for that!)
jsonSuccess() - shortcut to asJson(200)
jsonInvalid() - shortcut to asJson(400)
redirect(url) - send a redirect to the given redirect 
</pre>

As the Controller class extends the InputController, you can access both input and output/render/redirect related methods. For more, just  check out the Controller’s javadoc.

### Simple example

Check it out an example of how to use some of those methods:

<pre class="prettyprint">
public class IndexController extends Controller {
   public void postHello() {
       if (in("name").equals("octahedron")) {
           redirect("http://www.octahedron.com.br");
       } else {
           out("person", in("name"));
           success("hello.vm");
       }
   }
}
</pre>

## View

For simplicity, the framework has two option to render the view. One is Velocity template engine and the other is JSON. But the tool was built to render whatever you want (see _view_ package). It’s easy to implement another way of rendering a view.

You saw on the previous section how to use render methods.

** TODO: implementation of a custom view (e.g: freemarker) **

## Dependency Injection

Cotopaxi also comes with a simple dependency injection mechanism. This was created to internally usage, but we decided to open this possibility to developers too. It’s somehow inspired by Guice and you can see below how to use it.

<pre class="prettyprint">
public class ControllerOne extends Controller {
    @Inject
    private UserManager userManager;

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager
    }
}
</pre>

Pretty simple, uh? Well, this works on controllers and the dependencies of the class that are being injected on controller. For example, if UserManager has some dependency injection it will be solved cause UserManager injection was done on a controller. But if you have an isolated class using the annotation and the public method, this won’t work.

### Injection on isolated classes

Thinking of that, we provided another way to a class be self-injectable. Take a look:

<pre class="prettyprint">
public class IsolatedClass extends SelfInjectable {
    @Inject
    private SomeClass someClass;
    
    public void setSomeClass(SomeClass, someClass) {
        this.someClass = someClass;
    }
}
</pre>

### Dealing with interfaces

You can also inject implementations to interfaces dependencies. Cotopaxi is a framework based on developing multiple implementations to a common interface.

We didn’t cover it yet, but let’s supppose you want to use the Event Bus feature. You have to choose which Event Publisher you are gonna use. You chose AppEngineEventPublisher. Then you are going to use Event Bus all over your code without even writing a line about the publisher.
Go to the application.config file and add the follow line:

<pre class="prettyprint">
dependencies
    br.octahedron.cotopaxi.eventbus.EventPublisher (..) AppEngineEventPublisher
</pre>

Now you are going to use the App Engine event publisher without knowing the real implementation. If you want to chose to Amazon EC2 platform, you just need to change to the specific implementation and restart the app. Fucking awesome, uh?

## Boot loaders

It’s a mechanism to load anything you want before the app start up. Specify your boot loader class on application.config file:

<pre class="prettyprint">
bootloaders
    br.octahedron.MyBootloader
</pre>

Now you implements the interface Bootloader and the boot() method.

<pre class="prettyprint">
public class MyBootloader implements Bootloader {
    public void boot() {
        // anything you want here
    }
}
</pre>

This is useful when you want to maybe check if the database is created or anything like that.

## Interceptors

If you are already a Django developer you might know middlewares. The interceptors behave kind of middlewares, but we decided to separate then in three groups: ControllerInterceptor, TemplateInterceptors and FinalizerInterceptor. More details below.

All you need to do is specify the interceptors on application.config file:

<pre class="prettyprint">
interceptors
    br.octahedron.InterceptorOne
    br.octahedron.InterceptorTwo
</pre>

What will differ your interceptors is who you are extending from. The order you specified in the config file, it will be the same as execution.

### Controller’s interceptors

The controller interceptors are executed before the controller process the request. So, if you need to authenticate or authorize an user, using a controller interceptor sounds interesting. Another thing you need to know is that controller interceptors are used on your controller code with annotations.

Let’s see an example.

<pre class="prettyprint">
// AuthenticationInterceptor.class
public abstract class AuthenticationInterceptor extends ControllerInterceptor {
    public final void execute(Annotation ann) {
        if (this.currentUser() == null) {
            redirect("/user/sign_in");
        }
    }

    @Override
    public final Class&lt;? extends Annotation&gt; getInterceptorAnnotation() {
        return AuthenticationRequired.class;
    }
}

// AuthenticationRequired.class
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD, ElementType.TYPE})
public @interface AuthenticationRequired {
}
</pre>

Now, for this example, you can annotate a method or class to have authentication required. If you annotate a class, all the requests forwarded to the controller will have authenticationintercetor executing before. If you have just two of five actions that need authentication, you can annotate then separately. Code, code, code...

<pre class="prettyprint">
public class ControlerOne extends Controller {
    public void getIndex() { … }

    @AuthenticationRequired
    public void listUsers() { ... }
}
</pre>

Only when listUsers is called, you will have the interceptor being executed.

<pre class="prettyprint">
@AuthenticationRequired
public class ControlerOne extends Controller {
    public void getIndex() { ... }
    public void listUsers() { ... }
}
</pre>

Both getIndex() and listUsers() calls have interceptor being executed.

### Template interceptors

Template interceptors are executed after the controller process the request and before template render. It’s useful when you want to render a specific view, add repetitive attributes on output.

A particularity from template interceptors is that they are called every single response being rendered using a template (as you should guess, it isn’t execute when rendering a JSONResponse).

### Finalizer interceptors 

This kind of interceptors are executed after the Response be dispatched. You should use it if you want to do some house cleanning task after a request processing, as release database connection, for example.

Finalizers interceptor are always executed, even on errors.

## Logging

In Cotopaxi was developed a class for logging. It’s a simple java.util.Logger wrapper with a few fancy methods. We were tired of doing

<pre class="prettyprint">
log.info("User " + user.getName() + " was not found");
// TODO another example with two parameters
</pre>

We think this is better:

<pre class="prettyprint">
log.info("User %s was not found", user.getName());
log.info("User %s entered the password %s", user.getName(), user.getPassword());
// Please folks, don’t try it at home :-)
log.terror("Some error occurrs", exception);
</pre>

But before that, you need to instantiate the Log class:

<pre class="prettyprint">
Log log = new Log(SomeClass.class);
</pre>

The avaliable log levels are _debug_, _info_, _warning_, _error_. You can check it out more on the documentation [link]. It’s just something available on framework that you can use or not.
