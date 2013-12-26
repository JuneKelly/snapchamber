# snapchamber

A photo sharing app in Clojure and AngularJS


## Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

You will also need to have [nodejs](http://nodejs.org) and
[ruby](http://ruby-lang.org) installed.


## Running

To set up the project, install grunt, coffeescript and bower:
```
$ npm install -g grunt-cli bower coffee-script
```
depending on how you installed nodejs, you may need root permissions for
the above command. Subsequent commands should not be run with root
permissions.


Install the Sass ruby gem:
```
$ gem install sass
```


Then run the bootstrapper script, which will install npm and bower
packages, and also install the webdriver binaries needed to run the
protractor tests:
```
$ ./bin/bootstrap.sh
```


Finally, to run the development server:
```
$ grunt server
```


## Testing

Run the [protractor](https://github.com/angular/protractor) end-to-end tests:
```
$ grunt test:e2e
```
This will run the tests against the currently running server.


Run the clojure tests of the web service:
```
$ grunt test:ring
```


Or, run all tests in sequence:
```
$ grunt test:all
```


## License

Copyright © 2013 Shane Kilkelly, [MIT License](opensource.org/licenses/MIT)
