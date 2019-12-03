# CopyAndPaste

I made this project to test JSF 2.3 and websocket. There's a live demo [here](http://www.pablodubikin.com).


### How It Works

1. Enter some text in the textarea
2. Share the number in the url (pablodubikin.com/**5342**) with someone so they see your text

*Warning! Whoever enters, can update the contents of your text!*


### The story behind it

When we (at my company) were studying Spring Framework, the teacher kept needing us to copy stuff from him, to prevent us from trying to write everything he did. So instead, he used this web which was like _copynpaste.com_ or something.

Then like 2 or 3 months after, we went back to the site just to check how high the number of copied stuff had gotten, but found out the site was down.

Now, with the slogan *bring copynpaste back!* as an excuse, I created this site which basically does the same (maybe with some bonuses). 


### Why do this project?

I made this simple project to:

  - Create a simple skeleton in JSF 2.3 for some apps I have in mind.
  - Try websocket tech with JSF (I had tried with node and angular and liked it a lot).
  - Try Java's CDI (at my company we use Spring for our dependency injection).
  - Create an app to upload to my pablodubikin.com domain!


### Stuff it has

Here, a list of the stuff this project uses. And some stories about them. I write them down basically as a description of my *skeleton app*, to keep everything I'm using in check. 

#### MySQL

A simple *MySQL* database, with one table (`entry`). Each `entry` has `id, content, add_date`. And that's it.

*What I'm missing* is a bit more info like: `mod_date, user_agent, add_ip`. And obviously more surrounding tables like `user` (LOL), and so on.

#### Hibernate

On top of *MySQL* is *Hibernate* for the ORM. 

Nothing particular to say about it here. Just where I'm at: I know there's a way to feed my `hibernate.cfg.xml` from a `.properties` file, but haven't found out how. 

#### JSF 2.3

I use *JSF 2.2* at my company, which doesn't have the `websocket`, so I was eager to try it. As I said, I tried it using *node* and *angular*, and loved it. Turns out Java's version is cool too!

Right now the status of the  `websocket` is, it transmits to everyone. Each receiver is responsible of deciding whether it updates the user's `textarea` or not. 

I.e. 
>
> There are two users. One is conected to entry */1* and the other to entry *2*.
>
> When *user /1* writes something in his `textarea`, *user /2*'s browser receives it, but because they are viewing different `entrie`s, it will not display it. 
>
> As so happens with *user /1*, because the message is really triggered from the bean, and as I said is sent to *everyone*. *user /1*'s own `textarea` doesn't need to get updated either, since the user already has the correct content.
>
> If *user /1* opened a second browser window, entered the same url (/1), and placed it besides his first one, he would see the text updating as he writes.
>

To get around the *shoud I update the textarea?* problem I use (ClientWindow)[https://docs.oracle.com/javaee/7/api/javax/faces/lifecycle/ClientWindow.html], which I get from the `FacesContext` in the bean, and it distinguishes between user's tabs. So I just write down the `ClientWindow`'s id. 

*What I'm missing* is a way to have `websocket` only send messages to those who should receive them.


#### ocpsoft/rewrite

Previously known as `PrettyFaces`, (ocpsoft/rewrite)[https://github.com/ocpsoft/rewrite] is a cool project which allows you to do mappings like this:

```
<url-mapping id="test">
		<pattern value="/#{id}" />
		<view-id value="/app/welcome/welcome.xhtml" />
	</url-mapping>
```

So, instead of having an ugly url like www.whatever.com/app/welcome.xhtml?id=123, we have a simple one like www.whatever.com/123.

An interesting (enough) piece of trivia is that I couldn't make *ocpsoft/rewrite* and *JSF 2.3* work at the same time. So I asked around, and in (this stackoverflow question)[https://stackoverflow.com/q/58721840/3386085], user @selaron found out the problem came from an unimplemented method in some class.

I forked the project myself and found out it wasn't only that, but *ocpsoft/rewrite*'s `pom.xml` was declaring version `JSV 2.2`. So not only was the method's implementation missing, but it couldn't be added (because *JSF 2.2*'s interface didn't even declare it).

Finally I upgraded *ocpsoft/rewrite*'s dependencies and implemented the missing method, and it worked. Lincoln from *ocpsoft* ended up watching the whole thing, and updating the project himself, so it's by now available in maven central.


#### My Filter

Apart from *rewrite*'s, there's another filter in the app: `CreateEntryFilter.java`.

Its function is: whenever a user enters www.pablodubikin.com, if the url doesn't have a valid id at the end, after the slash, the filter creates a new `entry` in database, and redirects to */newEntrysId*.

Since this is a server side redirect, the user never sees anything weird. You can watch (or not-watch it) it live if you enter my site.


#### Some View Stuff

Here, I use the classics: *jQuery* and *Bootstrap* (4, for the first time), with [FontAwesome](https://fontawesome.com/) for icons. 

About *FontAwesome* and *JSF*: you have to remember to add the `#{facesContext.externalContext.requestContextPath}` to the urls in its css.

I also used [textarea-line-numbers](https://github.com/MatheusAvellar/textarea-line-numbers) to add the numbers on the left side of the `textarea`.

#### Some Other Stuff I'm Missing

I need to do a few things kind of urgently now:

 1. Create an admin page, to easily check what entries have been created lately, and check on them. I actually have like 800 entries and haven't entered that many times, or given the url to anybody.

 2. Add a *private* `radio-button` that lets creators prevent others from updating.
 
 3. Devise a way to detect when entries are created but not used. And think what to do about them.
 
 4. Add entrie's id at plain sight, so it's easier to see/copy? 


### Please, keep in mind

This project is not intended to be anything it's not, like a real live application for people to use. It's just a fun little project, which I completed by uploading it to an AWS and linking it to my domain.

So, if you see anything that might end up very badly, please contact me, instead of provoking that very bad thing. :)
