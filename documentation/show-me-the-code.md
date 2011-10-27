---
layout: show-me-the-code
---
# Show me the code!

We are gonna make a **Todo list** application as our *Hello World*.

After you get the blank project and configure it using the [ctpx tool](/documentation/using-cotopaxi.html#using_ctpx_tool) to also use the extensions, all you need to do is follow the steps below.

## application.config file

For this example, we just need to modify two of the sections: properties and controllers. We need to specify the application base url and the controller with its own actions.

The application base url chosen for this app was *[cotopaxi-todolist.appspot.com](http://cotopaxi-todolist.appspot.com)*. We also added one controller that has the basic CRUD operations as its actions.

<pre class="prettyprint">
// application.config
properties
    APPLICATION_BASE_URL http://cotopaxi-todolist.appspot.com/

(...)

controllers
    br.octahedron.cotopaxi.todolist.controller.TodoController
        /           get     List
        /create     post    Create
        /update     post    Update
        /delete     post    Delete
</pre>

We could set the *create* and *delete* http method as *put* and *delete* respectively, but App Engine does not support these methods for now.

If you want to see more about application.config, see the [core documentation](/documentation/core.html) section.

## The controller

We created a TodoController class that extends from Controller and wrote the respective methods to the declared actions oh the application.config file.

<pre class="prettyprint">
// TodoController.java
public class TodoController extends Controller {
    private static final String BASE_TPL = "todo/list.vm";
    private static final String BASE_URL = "/";
    
    @Inject
    private TodoDAO todoDAO;
    
    /*
     * used by dependency injection
     */
    public void setTodoDAO(TodoDAO todoDAO) {
        this.todoDAO = todoDAO;
    }
    
    public void getList() {
        out("todos", todoDAO.getAllDesc());
        success(BASE_TPL);
    }
    
    public void postCreate() {
        Todo todo = new Todo(in("description"));
        todoDAO.save(todo);
        redirect(BASE_URL);
    }
    
    public void postUpdate() throws ConvertionException {
        Todo todo = todoDAO.get(in("id", Builder.number(NumberType.LONG)));
        todo.setCompleted(in("completed", Builder.bool()));
        todoDAO.save(todo);
        jsonSuccess();
    }
    
    public void postDelete() throws ConvertionException {
        todoDAO.delete(in("id", Builder.number(NumberType.LONG)));
        jsonSuccess();
    }
}
</pre>

See that we are using the dependency injection feature to inject the DAO. This feature is supported by the core and made your code simpler and elegant.

On the *getList()* method, we put all the todos on the output variable *todos* using the *out()* helper method. Keep in mind that variable name cause we are gonna use it in the [view](#the_view) to access its content. 

If you want to see more about controllers and helper methods, see the [core documentation](/documentation/core.html#controllers) section.

<small>P.S.: We do not recommend you to use the DAO directly on your controller! This is just a proof of concept.</small>

## The model

The model is quite simple, it just has a description and a boolean variable to check if the task is completed or not.

Remember that by default the extensions use the Java Data Objects (JDO) as the standard interface for storing objects into the database. So, the annotations used on the coded below belongs to the JDO library. Don't worry.

<pre class="prettyprint">
// Todo.class
@PersistenceCapable
public class Todo implements Serializable {
    private static final long serialVersionUID = 7892638707825018254L;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
    @Persistent
    private String description;
    @Persistent
    private boolean isCompleted;
    
    public Todo(String task) {
        this.description = task;
    }
    
    public Long getId() {
        return this.id;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public boolean isCompleted() {
        return this.isCompleted;
    }
    
    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
}
</pre>

That's it!

If you want to see more about JDO, see [App Engine documentation](http://code.google.com/appengine/docs/java/datastore/jdo/). 

### Creating the DAO

The model is done, awesome. But how do we save or delete and object? We used a common design pattern named Data Access Object (DAO).

We just need to create a class that extends the [GenericDAO](/javadoc/extensions/br/octahedron/cotopaxi/datastore/jdo/GenericDAO.html) as below. We automatically has the basic operations implemented, but you can override or create your custom queries.

<pre class="prettyprint">
// TodoDAO.class
@SuppressWarnings("unchecked")
public class TodoDAO extends GenericDAO&lt;Todo&gt; {
    public TodoDAO() {
        super(Todo.class);
    }
}
</pre>

#### Creating a custom query

We decided that our application would list the tasks in descending order of addition. The recent tasks are listed first. 

The default *getAll()* does not do that, then we need to implement a custom query for that. See below.

<pre class="prettyprint">
// TodoDAO.class
@SuppressWarnings("unchecked")
public class TodoDAO extends GenericDAO&lt;Todo&gt; {
    (...)

    public Collection&lt;Todo&gt; getAllDesc() {
        Query query = this.datastoreFacade.createQueryForClass(Todo.class);
        query.setOrdering("id descending");
        return (Collection&lt;Todo&gt;) query.execute();
    }
}
</pre>

Simple, uh?

Let's caught the front-end now!

## The view

You already created the model and coded correctly the controller, now you need to show the content. Ugh! That's so obvious. Tisk, tisk, tisk... 

We used [Velocity](http://velocity.apache.org/) as the template engine but you can also use JSP/JSTL if you want.

<pre class="prettyprint">
(...)

#foreach( $todo in $todos )
    &lt;li&gt;
        &lt;div class="todo" data-id="$todo.id"&gt;
            &lt;input type="checkbox" #if ( $todo.completed )checked="checked"#end /&gt;
            &lt;span class="description #if ( $todo.completed ) done#end"&gt;$todo.description&lt;/span&gt;
            &lt;span class="delete"&gt;&lt;/span&gt;
        &lt;/div&gt;
    &lt;/li&gt;
#end

(...)
</pre>

If you want to see more about Velocity, see its [user guide](http://velocity.apache.org/engine/releases/velocity-1.7/user-guide.html).

## Checkout demo

![Todo list screenshost](/img/screenshot.jpg)

You can check it out live [here](cotopaxi-todolist.appspot.com) and the source code [here](http://github.com/octahedron/cotopaxi-todolist/). Have fun!

If any doubts, do not hesitate in contact us!