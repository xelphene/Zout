# Zout

Zout is a Servlet container backdoor. Features include:

  * Pure servlet

  * HTTP authentication

  * Password specified at compile time and included in war file automatically

  * Password stored hashed (PBKDF2-based hashing)

  * Specify command to execute as one string with space-separated argv
    components or as explicit individual argv parameters.

# Build instructions

Use gradle to compile a war file and specify the password:

```
> gradle war -Ppassword=asdf222
```

A war file will then appear at servlet/build/libs/servlet.war. Deploy on
your favorite insecure servlet container.  Browse to the servlet's deployed
URL and specify a command to execute with either a single "argv" query
string parameter with spaces separating each argv component or with
individually numbered argv<N> parameters.  Examples:

```
http://localhost:8021/servlet/?argv=/bin/ls%20/tmp
```
```
http://localhost:8021/servlet/?argv0=/bin/sh&argv1=-c&argv2=echo%20hi222%20%3E%20/tmp/hi222
```

The password you specified at compile time will be required via HTTP
authentication.  The username doesn't matter.

# Author

Zout was written by Steve Benson for Hurricane Labs.
