Sourced from https://source.android.com/setup/contribute/code-style
Content and code samples on this page are subject to the licenses described in the Content License.
Java and OpenJDK are trademarks or registered trademarks of Oracle and/or its affiliates.

~~~~~~~~~~~~~~~~~~~~~
Fully qualify imports
~~~~~~~~~~~~~~~~~~~~~
When you want to use class Bar from package foo, there are two possible ways to import it:

import foo.*;
Potentially reduces the number of import statements.

import foo.Bar;
Makes it obvious what classes are used and the code is more readable for maintainers.

Use import foo.Bar; for importing all Android code. An explicit exception is made for Java standard libraries (java.util.*, java.io.*, etc.) and unit test code (junit.framework.*).

~~~~~~~~~~~~~~~~~~
Java library rules
~~~~~~~~~~~~~~~~~~
There are conventions for using Android's Java libraries and tools.
In some cases, the convention has changed in important ways and older code might use a deprecated pattern or library.
When working with such code, it's okay to continue the existing style. When creating new components however, never use deprecated libraries.

~~~~~~~~~~~~~~~~
Java style rules
~~~~~~~~~~~~~~~~
Use Javadoc standard comments
Every file should have a copyright statement at the top, followed by package and import statements (each block separated by a blank line), and finally the class or interface declaration.
In the Javadoc comments, describe what the class or interface does.

Every class and nontrivial public method that you write must contain a Javadoc comment with at least one sentence describing what the class or method does.
This sentence should start with a third person descriptive verb.

/**
 * Constructs a new String by converting the specified array of
 * bytes using the platform's default character encoding.
 */
public String(byte[] bytes) {
    ...
}

~~~~~~~~~~~~~~~~~~~~~~~
Order import statements
~~~~~~~~~~~~~~~~~~~~~~~
The ordering of import statements is:

Android imports
Imports from third parties (com, junit, net, org)
java and javax
To exactly match the IDE settings, the imports should be:

Alphabetical within each grouping, with capital letters before lower case letters (for example, Z before a)
Separated by a blank line between each major grouping (android, com, junit, net, org, java, javax)
Originally, there was no style requirement on the ordering, meaning IDEs were either always changing the ordering or IDE developers had to disable the automatic import management features and manually maintain the imports. This was deemed bad. When Java-style was asked, the preferred styles varied wildly and it came down to Android needing to simply "pick an ordering and be consistent." So we chose a style, updated the style guide, and made the IDEs obey it. We expect that as IDE users work on the code, imports in all packages will match this pattern without extra engineering effort.

We chose this style such that:

The imports that people want to look at first tend to be at the top (android).
The imports that people want to look at least tend to be at the bottom (java).
Humans can easily follow the style.
IDEs can follow the style.
Put static imports above all the other imports ordered the same way as regular imports.

~~~~~~~~~~~~~~~~~~~~~~~~~~
Use spaces for indentation
~~~~~~~~~~~~~~~~~~~~~~~~~~
We use four (4) space indents for blocks and never tabs. When in doubt, be consistent with the surrounding code.

We use eight (8) space indents for line wraps, including function calls and assignments.

~~~~~~~~~~~~~~~~~~~~~~~~
Use standard brace style
~~~~~~~~~~~~~~~~~~~~~~~~
Put braces on the same line as the code before them, not on their own line:
class MyClass {
    int func() {
        if (something) {
            // ...
        } else if (somethingElse) {
            // ...
        } else {
            // ...
        }
    }
}

We require braces around the statements for a conditional.
Exception: If the entire conditional (the condition and the body) fit on one line, you may (but are not obligated to) put it all on one line.
For example, this is acceptable:

if (condition) {
    body();
}

and this is acceptable:

if (condition) body();

but this is not acceptable:

if (condition)
    body();  // bad!

~~~~~~~~~~~~~~~~~~~
Write short methods
~~~~~~~~~~~~~~~~~~~
When feasible, keep methods small and focused. We recognize that long methods are sometimes appropriate, so no hard limit is placed on method length.
If a method exceeds 40 lines or so, think about whether it can be broken up without harming the structure of the program.

~~~~~~~~~~~~~~~~~~~~
Limit variable scope
~~~~~~~~~~~~~~~~~~~~
Keep the scope of local variables to a minimum. This increases the readability and maintainability of your code and reduces the likelihood of error.
Declare each variable in the innermost block that encloses all uses of the variable.

Declare local variables at the point where they are first used. Nearly every local variable declaration should contain an initializer.
If you don't yet have enough information to initialize a variable sensibly, postpone the declaration until you do.

The exception is try-catch statements. If a variable is initialized with the return value of a method that throws a checked exception, it must be initialized inside a try block.
If the value must be used outside of the try block, then it must be declared before the try block, where it can't yet be sensibly initialized:

// Instantiate class cl, which represents some sort of Set

Set s = null;
try {
    s = (Set) cl.newInstance();
} catch(IllegalAccessException e) {
    throw new IllegalArgumentException(cl + " not accessible");
} catch(InstantiationException e) {
    throw new IllegalArgumentException(cl + " not instantiable");
}

// Exercise the set
s.addAll(Arrays.asList(args));

However, you can even avoid this case by encapsulating the try-catch block in a method:

Set createSet(Class cl) {
    // Instantiate class cl, which represents some sort of Set
    try {
        return (Set) cl.newInstance();
    } catch(IllegalAccessException e) {
        throw new IllegalArgumentException(cl + " not accessible");
    } catch(InstantiationException e) {
        throw new IllegalArgumentException(cl + " not instantiable");
    }
}

...

// Exercise the set
Set s = createSet(cl);
s.addAll(Arrays.asList(args));

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Follow field naming conventions
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Non-public, non-static field names start with m.
Static field names start with s.
Other fields start with a lower case letter.
Static final fields (constants, deeply immutable) are ALL_CAPS_WITH_UNDERSCORES.

For example:
public class MyClass {
    public static final int SOME_CONSTANT = 42;
    public int publicField;
    private static MyClass sSingleton;
    int mPackagePrivate;
    private int mPrivate;
    protected int mProtected;
}

~~~~~~~~~~~~~~~~~
Limit line length
~~~~~~~~~~~~~~~~~
Each line of text in your code should be at most 100 characters long. While much discussion has surrounded this rule, the decision remains that 100 characters is the maximum with the following exceptions:

If a comment line contains an example command or a literal URL longer than 100 characters, that line may be longer than 100 characters for ease of cut and paste.
Import lines can go over the limit because humans rarely see them (this also simplifies tool writing).

~~~~~~~~~~~~~~~~~~~~~
Javatests style rules
~~~~~~~~~~~~~~~~~~~~~
Follow test method naming conventions and use an underscore to separate what's being tested from the specific case being tested.
This style makes it easier to see which cases are being tested.

For example:
testMethod_specificCase1 testMethod_specificCase2

void testIsDistinguishable_protanopia() {
    ColorMatcher colorMatcher = new ColorMatcher(PROTANOPIA)
    assertFalse(colorMatcher.isDistinguishable(Color.RED, Color.BLACK))
    assertTrue(colorMatcher.isDistinguishable(Color.X, Color.Y))
}