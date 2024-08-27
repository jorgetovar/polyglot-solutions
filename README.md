# The Clojure Paradox
Several years ago, I came across The Python Paradox by Paul Graham. In it, Graham argues that Lisp and Python programmers are often those who genuinely care about programming and, as a result, tend to do it better than, for instance, Java programmers. This perspective fundamentally changed how I viewed Python as a language.

At first, I found Python’s lack of semicolons and reliance on indentation to be strange and uncomfortable. I even saw Python as a tool for building only basic applications. However, with the rise of serverless computing, machine learning, and data science, the immense power of Python has become increasingly apparent. The language is getting faster, and its ecosystem is rapidly growing. Libraries like FastAPI and Pandas are truly remarkable, allowing us to solve problems succinctly. As programmers, our job is to solve problems, and since we read more code than we write, having fewer lines of code reduces the surface area for bugs to hide and helps us avoid cognitive overload.

When I started working with AWS's Boto3, I realized that tasks that previously took me 30 lines of Java could now be done in just 3 lines of Python. It was mind-blowing. Don’t get me wrong, Java is still one of my favorite programming languages, and with its new release cadence, it’s only getting better. But the amount of ceremony required to accomplish basic tasks in Java is something sometimes we’d all prefer to avoid.

Recently, I've been experimenting with Go. Although it prides itself on simplicity, IMHO *I find it too verbose*. I know that excellent tools have been built with Go, and there are certain ideas and applications that should be developed with it. Its compilation speed and efficient memory usage make Go a strong contender, it might even be the best combination of developer experience and performance, which is becoming increasingly important in modern, cloud-native applications.

However, after 10 years in the industry and having deployed applications in several languages, I remain a fan of Clojure. Despite its niche status, Clojure incorporates ideas from other languages, such as Go’s goroutines. It’s a Lisp dialect, inherently immutable, and designed with concurrency in mind. What stands out most to me is how Clojure allows you to **focus on solving problems without the burden of unnecessary ceremony**. The majority of the code is about the problem itself; it’s data-oriented, and I often find that it helps me enter a *Flow state* (Happiness) where programming becomes truly enjoyable.

With Go, I currently have mixed feelings. While it brings many good ideas to the table in terms of concurrency and simplicity, I find that the codebases tend to be larger and more ceremonious. Clojure, on the other hand, tends to produce code that is less brittle and primarily composed of pure functions.

## Conclusion
I’ve always been a fan of timeless ideas because they are often the most important and foundational, yet they are also the most overlooked and I feel that Clojure fully embraces of all them.

- Programs composed mostly of pure functions are more robust and easier to test.
- Immutability reduces complexity.
- Smaller programs have fewer bugs.
- Software development is fundamentally about composition.