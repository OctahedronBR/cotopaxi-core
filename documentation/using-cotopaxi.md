---
layout: using-cotopaxi
---
# Using Cotopaxi

## Prerequisites

To use the Cotopaxi framework, we recommend you to use Java 5 or later and Maven 2 or later.

If you are using MacOS, Java is already built-in. If you are using Linux, make sure to use either the Sun-JDK or OpenJDK. If you are using Windows, download and install the latest JDK package.

## Downloading blank project

Unlike other frameworks that forces you to use a stricted folder structure. The only thing you need to do is place the application.config file in your WEB-INF folder. Even so, we encourage you to use our blank project that uses Maven as project manager.

You have two options to get the blank project. You can download it via Git

<pre class="prettyprint">
git clone git://github.com/Octahedron/cotopaxi-blank-project.git
</pre>

but remember to remove the .git folder so you can use your own repository. Or you can download via HTTP

<pre class="prettyprint">
https://github.com/Octahedron/cotopaxi-blank-project/tarball/master
</pre>

Now, you have the necessary folder structure to start your appplication. 

## Configuring pom.xml

As mentioned before, Maven is 'required' to use the Cotopaxi framework. You can set up things manually but we don't recommend neither teach you to do that.

You can configure pom.xml manually or [use the ctpx tool](#using_ctpx_tool) if you are using Linux (specifically Ubuntu).

The configuration that has to be done is rename the pom.xml.sample to pom.xml and define a project and package name. See an example below:

<pre class="prettyprint">
&lt;groupId&gt;br.octahedron&lt;/groupId&gt;
&lt;artifactId&gt;todolist&lt;/artifactId&gt;
&lt;version&gt;1.0-SNAPSHOT&lt;/version&gt;
&lt;packaging&gt;war&lt;/packaging&gt;
&lt;name&gt;todolist&lt;/name&gt;
</pre>

If you want to use the Cotopaxi Extensions that also includes JDO enhancer plugin, Google App Engine SDK, JUnit and EasyMock, add the code below on plugins section:

<pre class="prettyprint">
&lt;!-- Datanucleus Enhancer Plugin --&gt;
&lt;plugin&gt;
    &lt;groupId&gt;org.datanucleus&lt;/groupId&gt;
    &lt;artifactId&gt;maven-datanucleus-plugin&lt;/artifactId&gt;
    &lt;version&gt;1.1.4&lt;/version&gt;
    &lt;configuration&gt;
        &lt;mappingIncludes&gt;**/*.class&lt;/mappingIncludes&gt;
        &lt;verbose&gt;true&lt;/verbose&gt;
        &lt;api&gt;JDO&lt;/api&gt;
    &lt;/configuration&gt;
    &lt;executions&gt;
        &lt;execution&gt;
            &lt;phase&gt;process-classes&lt;/phase&gt;
            &lt;goals&gt;
                &lt;goal&gt;enhance&lt;/goal&gt;
            &lt;/goals&gt;
        &lt;/execution&gt;
    &lt;/executions&gt;
    &lt;dependencies&gt;
        &lt;dependency&gt;
            &lt;groupId&gt;javax.jdo&lt;/groupId&gt;
            &lt;artifactId&gt;jdo2-api&lt;/artifactId&gt;
            &lt;version&gt;2.3-ec&lt;/version&gt;
            &lt;scope&gt;runtime&lt;/scope&gt;
        &lt;/dependency&gt;
        &lt;dependency&gt;
            &lt;groupId&gt;org.datanucleus&lt;/groupId&gt;
            &lt;artifactId&gt;datanucleus-core&lt;/artifactId&gt;
            &lt;version&gt;1.1.5&lt;/version&gt;
            &lt;type&gt;jar&lt;/type&gt;
            &lt;scope&gt;runtime&lt;/scope&gt;
            &lt;exclusions&gt;
                &lt;exclusion&gt;
                    &lt;groupId&gt;javax.transaction&lt;/groupId&gt;
                    &lt;artifactId&gt;transaction-api&lt;/artifactId&gt;
                &lt;/exclusion&gt;
            &lt;/exclusions&gt;
        &lt;/dependency&gt;
    &lt;/dependencies&gt;
&lt;/plugin&gt;
</pre>

And those on dependencies section:

<pre class="prettyprint">
&lt;!-- Cotopaxi Extensions --&gt;
&lt;dependency&gt;
    &lt;groupId&gt;br.octahedron.cotopaxi&lt;/groupId&gt;
    &lt;artifactId&gt;extensions&lt;/artifactId&gt;
    &lt;version&gt;1.0-SNAPSHOT&lt;/version&gt;
    &lt;type&gt;jar&lt;/type&gt;
    &lt;scope>compile&lt;/scope&gt;
&lt;/dependency&gt;
&lt;!-- GAE Dependencies --&gt;
&lt;dependency&gt;
    &lt;groupId&gt;com.google.appengine&lt;/groupId&gt;
    &lt;artifactId&gt;appengine-testing&lt;/artifactId&gt;
    &lt;version&gt;1.5.4&lt;/version&gt;
    &lt;type&gt;jar&lt;/type&gt;
    &lt;scope&gt;test&lt;/scope&gt;
&lt;/dependency&gt;
&lt;dependency&gt;
    &lt;groupId&gt;com.google.appengine&lt;/groupId&gt;
    &lt;artifactId&gt;appengine-api-stubs&lt;/artifactId&gt;
    &lt;version&gt;1.5.4&lt;/version&gt;
    &lt;type&gt;jar&lt;/type&gt;
    &lt;scope&gt;test&lt;/scope&gt;
&lt;/dependency&gt;
&lt;!-- Test dependencies --&gt;
&lt;dependency&gt;
    &lt;groupId&gt;junit&lt;/groupId&gt;
    &lt;artifactId&gt;junit&lt;/artifactId&gt;
    &lt;version&gt;4.8.2&lt;/version&gt;
    &lt;type&gt;jar&lt;/type&gt;
    &lt;scope&gt;test&lt;/scope&gt;
&lt;/dependency&gt;
&lt;dependency&gt;
    &lt;groupId&gt;org.easymock&lt;/groupId&gt;
    &lt;artifactId&gt;easymock&lt;/artifactId&gt;
    &lt;version&gt;3.0&lt;/version&gt;
    &lt;type&gt;jar&lt;/type&gt;
    &lt;scope&gt;test&lt;/scope&gt;
&lt;/dependency&gt;
</pre>

Well, for your lucky and safety there is ctpx tool to do that for you. Check it out on next section.

## Using ctpx tool

The ctpx tool was built on top of Ubuntu and shell script. It uses Maven to manage more friendly your project.