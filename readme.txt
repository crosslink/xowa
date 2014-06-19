*************
RELEASE NOTES
*************

v1.6.3.1 features the following:

* Options to automatically collapse / expand TOC, Collapsible Table, NavFrame. See [[Help:Options/Wiki/HTML]] and [[Help:Diagnostics/Javascript/All]]
* Minor changes to javascript / HTML head generation
* Offline images for Italian (update) and Indonesian (new) wikis

See the CHANGE LOG below for a complete list of items specific to this release.

*******
CONTACT
*******

XOWA's website is at http://xowa.sourceforge.net/

If you encounter issues, please post to https://sourceforge.net/p/xowa/discussion/


*******
INSTALL
*******

FILES
=====
* There are two types of files:
  * The xowa_app_*** files are for new users. They will have all the files necessary for xowa to run, including the main jars, XulRunner, languages, etc..
  * The xowa_upgrade_*** files are for upgrade users. They will have the xowa jar and any other files specific to the release.
    Note that upgrade users can also download the xowa_app_*** files and unzip it over their current version. The xowa_upgrade_*** files are provided as a convenience.
* The paths below are recommendations. Please feel free to choose any other.
* If you've received the readme.txt offline, all files are at https://sourceforge.net/projects/xowa/files/


REQUIREMENTS
============
* Java 1.7+
  If you do not have Java 1.7 installed, then please install 1.7 from http://www.java.com/en/download/manual.jsp
  On Ubuntu Linux, you can use OpenJDK by running 'sudo apt-get install openjdk-7-jre'.
* Compression/Decompression program ("unzip")
  The XOWA files will be compressed. If your Operating System does not natively support decompressing, please install 7-Zip from http://7-zip.org.
* 512 MB RAM
  XOWA can run on lower memory machines, but 256 MB is needed for importing the larger wikis (EX: en.wikipedia.org; commons.wikimedia.org)

Linux
-----
* Download 'xowa_app_linux_v*.*.*.*.zip'
* Unzip to '/home/your_user_name/xowa/'. When you are done you should have a file called '/home/your_user_name/xowa/xowa_linux.jar' as well as many others
* Open a terminal and run "sh /home/your_user_name/xowa/xowa_linux.sh"
* If "xowa_linux.sh" doesn't work, run "java -Xmx256m -jar /home/your_user_name/xowa/xowa_linux.jar"

Mac OS X 64-bit
---------------
* Download 'xowa_app_macosx_64_v*.*.*.*.zip'
* Unzip to '/Users/your_user_name/xowa/'. When you are done you should have a file called '/Users/your_user_name/xowa/xowa_macosx_64.jar' as well as many others
* Open a terminal by doing Finder -> Applications -> Utilities -> Terminal
* Run "sh /Users/your_user_name/xowa/xowa_macosx_64.sh"
  If you get a "No Java runtime present" you will need to set up Java on your machine. There are two options:
  * Update to the latest Java 6 by going to http://support.apple.com/kb/DL1572?viewlocale=en_US
    This will be simpler. However, note that Java 6 is an older obsolete version.
  * Download Java 7 by going to java.com
    This will be the most up-to-date Java version. However it requires additional steps.
    * Find your java runtime: Run "/Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/bin/java -version" in the terminal to verify the path
    * Assuming your java is at the above location, run XOWA by doing either of the following
      (1) Run the following from the terminal:
          /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/bin/java -Xmx256m -XstartOnFirstThread -Xdock:name=XOWA /Users/your_user_name/xowa/xowa_macosx_64.jar
      (2) Change your official java to the plugin version and then run xowa_macosx_64.sh
          sudo ln -fs /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/bin/java /usr/bin/java          
NOTE: if the XOWA menu bar is not clickable, alt-tab to the terminal and back to XOWA. The menu bar will then be usable

Mac OS X 32-bit
---------------
* Download 'xowa_app_macosx_v*.*.*.*.zip'
* Unzip to '/Users/your_user_name/xowa/'. When you are done you should have a file called '/Users/your_user_name/xowa/xowa_macosx.jar' as well as many others
* Open a terminal by doing Finder -> Applications -> Utilities -> Terminal
* Run "sh /Users/your_user_name/xowa/xowa_macosx.sh"
* If "xowa_macosx.sh" doesn't work, run "java -Xmx256m -d32 -XstartOnFirstThread -jar /Users/your_user_name/xowa/xowa_macosx.jar"

Windows
-------
If XOWA fails to launch after the steps below, please check the following:

* Administrator privileges: If you're on Windows Vista, 7, or 8, try running the program with administrator privileges. You can do so by right-clicking on the exe and choosing "Run as administrator"

* XULRunner: Try double-clicking the xulrunner.exe.
**    Go to C:\xowa\bin\windows_64\xulrunner (or on 32-bit machines, C:\xowa\bin\windows\xulrunner)
**    Double-click xulrunner.exe
**    You should get a message box that starts off with "Mozilla XUL Runner 10.0.10"
**    If it fails with "Can't start because MSVCR100.dll is missing...try reinstalling to fix...", then you will need to the Visual C++ redistributable. See https://sourceforge.net/p/xowa/discussion/general/thread/cc7d867d/
  
* Java installation: Check that your Java installation is installed correctly
**    Open a command window by clicking on the Start button, selecting Run and typing in "cmd" (no quotes)
**    Type the following in the terminal window: "java -version" (no quotes)
**    It should report "java version "1.6****" or higher

Windows 32-bit
--------------
* Download 'xowa_app_windows_v*.*.*.*.zip'
* Unzip to 'C:\xowa'. When you are done you should have a file called 'C:\xowa\xowa.exe' as well as many others
* Double-click 'xowa.exe'
* If 'xowa.exe' fails to launch, you can also try double-clicking 'xowa_windows.jar'
* If double-clicking doesn't work, you can also try the following:
** Start -> Run > "cmd"
** Enter "java -Xmx256m -jar C:\xowa\xowa_windows.jar"

Windows 64-bit (using 64-bit JRE)
---------------------------------
* Download 'xowa_app_windows_64_v*.*.*.*.zip'
* Unzip to 'C:\xowa'. When you are done you should have a file called 'C:\xowa\xowa_64.exe' as well as many others
* Double-click 'xowa_64.exe'
* If 'xowa_64.exe' fails to launch, you can also try double-clicking 'xowa_windows_64.jar'
* If double-clicking doesn't work, you can also try the following:
** Start -> Run > "cmd"
** Enter "java -Xmx256m -jar C:\xowa\xowa_windows_64.jar"

Windows 64-bit (using 32-bit JRE)
---------------------------------
* Download 'xowa_app_windows_v*.*.*.*.zip'
* Unzip to 'C:\xowa'. When you are done you should have a file called 'C:\xowa\xowa.exe' as well as many others
* Visit http://www.oracle.com/technetwork/java/javase/downloads/index.html
* Click the "Download" button under "JRE"
** Choose the Windows x86 version
*** As of the time of this writing, "Windows x86 Offline" links to http://download.oracle.com/otn-pub/java/jdk/7u40-b43/jre-7u40-windows-i586.exe
** Choose an installation folder. For example, "C:\Program Files (x86)\Java\jre7_x86"
** After installing, run the following in cmd: "C:\Program Files (x86)\Java\jre7_x86\bin\java" -Xmx256m -jar C:\xowa\xowa_windows.jar


*****
USAGE
*****

Setting up Simple Wikipedia
===========================
* Once XOWA loads, you will see the Main Page
* Click the link for "Set up Simple Wikipedia". Wait about 3 minutes for the wiki to download and install. When it is finished, it will open Simple Wikipedia
* Browse Simple Wikipedia. When you are done, click on the Main Page link under XOWA in the left hand navigation bar. 

Setting up images
=================
* Click the link for "Set up images (Windows)". Wait about 3 minutes for the image programs to install.
* Open the "Wikis" list in the sidebar and click on "simple.wikipedia.org"
* Images will now download for the Main Page. They will automatically download for any page you visit.
  Here are some example pages to visit (you can copy and paste these into the address bar):
    http://simple.wikipedia.org/wiki/Gothic_architecture
    http://simple.wikipedia.org/wiki/Saturn_(planet)
    http://simple.wikipedia.org/wiki/Chess
    http://simple.wikipedia.org/wiki/World_History

Setting up other wikis
======================
* If you want to try any other wiki, click on "list of data dumps" on the XOWA Main Page. You can also navigate to "Help:Import/List"

Downloading offline thumbs
==========================
* If you want to download a complete set of images for your wiki, see the following links:
** For instructions, see: http://xowa.sourceforge.net/setup_simplewiki.html and http://xowa.sourceforge.net/setup_enwiki.html
** For a list of image databases see http://xowa.sourceforge.net/image_dbs.html or https://archive.org/search.php?query=xowa


**********
CHANGE LOG
**********
** package **
* Package: Release update for Italian Wikipedia.
* Package: Release bundle for Italian sister wikis.
* Package: Release bundle for all Indonesian wikis.

** fix **
* Gui.Tabs: Fix tab options not restored for Tab Height, Close Visible and Unselected Close Visible. See: [[Help:Options/Tabs]] [broken since:start] 
* Options: Escape HTML characters in <textarea> (affects: content code format incorrectly using "<pre>"). See: [[Help:Options/Wiki/HTML]] [broken since:v1.6.1.1] 

** add **
* Javascript: Add gadget to show / hide NavFrames. See: https://en.wikipedia.org/wiki/Teresa_of_Ávila
* Javascript: Add option to control default show / hide state of Table of Contents. See: [[Help:Options/Wiki/HTML]] [[Help:Diagnostics/Javascript/Table_of_contents]]
* Javascript: Add option to control default show / hide state of collapsible tables. See: [[Help:Options/Wiki/HTML]] [[Help:Diagnostics/Javascript/Collapsible]]
* Javascript: Add option to control default show / hide state of NavFrames. See: [[Help:Options/Wiki/HTML]] [[Help:Diagnostics/Javascript/NavFrame]]
* Gui.Tab: Add option to hide tab bar if 1 or 0 tabs {suggested by Schnark}. See: [[Help:Options/Tabs]]
* Gui.Url_bar: Add shortcut to open multiple lines in separate tabs; EX:Copy "a\nb" to clipboard -> Ctrl + Enter.

** significant **
* Javascript: Change xowa.ready.js to allow unaltered usage of MediaWiki scripts {contributed by Schnark}.
* Scribunto: Add namespace to Frame_title; EX:{{#invoke:A|B}} has frame_title of "Module:A" not "A" {detected by Schnark}. See: https://de.wikipedia.org/wiki/Wikipedia:Technik/Linkbox https://en.wikipedia.org/wiki/Wikipedia:WikiProject_Articles_for_creation/Submissions/List https://fr.wikipedia.org/wiki/Liste_des_monuments_historiques_du_Nord_(A-L)
* Html.Tidy: Limit tidy / JTidy to page content not entire page (affects: one page being rendered incorrectly b/c tidy / JTidy incorrectly merges correct content with correct enclosing HTML). See: https://it.wikiquote.org/wiki/Indro_Montanelli
* Html: Add <xowa_html> node to encapsulate <style> <script> in home wiki. See: [[Help:Options]]

** minor **
* Lang: Update translations for German {contributed by Schnark}.
* References: Don't show backlinks in tooltips for references used multiple times  {contributed by Schnark}. See: https://en.wikipedia.org/wiki/List_of_Russula_species
* Messages: Do not show pages in [[MediaWiki:]] namespace with gfs syntax; EX:$ should display as $, not ~{0} {detected by Schnark}. See: https://de.wikipedia.org/wiki/MediaWiki:Gadget-toolserver-integration.js https://en.wikipedia.org/wiki/MediaWiki:filetype-unwanted-type
* Gallery: Handle empty link argument in gallery (affects: page not loading); EX:A.png|link= {detected by Schnark}. See: https://de.wikipedia.org/wiki/Wikipedia:Fragen_zur_Wikipedia/Archiv/2014/Woche_20 
* Html.JTidy: Do not remove <hr> (affects: <hr/> not showing). See: https://en.wikipedia.org/wiki/Portal:Current_events/2006_September_7
* Html.Toc: Handle tocs wherein first header is larger than second (affects: deeply nested tocs on several Wikipedia:Articles for creation with incorrect header layout); EX:=== a ===\n==b==. See: https://en.wikipedia.org/wiki/Wikipedia:Articles_for_creation/2006-08-27 https://en.wikipedia.org/wiki/Wikipedia:Articles_for_creation/2006-03-04 https://en.wikipedia.org/wiki/Wikipedia:Articles_for_creation/2007-05-06
* Parser.Link: Ignore widths larger than Int_.MaxValue (2.147 billion) (affects: wide horizontal scroll bar on many idwiki pages). See: https://id.wikipedia.org/wiki/Baho
* Gui.Lang: Change portal tabs to use wiki's language, not user's language (affects: Page, Read, Edit tabs always showing in uers's language, and not switching based on wiki's language); EX:xowa.gfs app.user.msgs -> msgs. See: https://id.wiktionary.org/wiki/Main_Page [broken since:v1.5.2.1] 

** trivial **
* Gui: Add default shortcut for Font Reset; EX:Ctrl + 0 {requested by Anselm}. See: [[Help:Options/Shortcuts]]

** doc **
* Install: Add troubleshooting not to run xulrunner. See: readme.txt

** dev **
* Source: Allow same object to be added multiple times to GfsCore; EX:"app", "xowa".

