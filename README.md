<div id="top"></div>

[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]
[![LinkedIn][linkedin-shield]][linkedin-url]



<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/falistra/todolist">
    <img src="images/todolist.png" alt="Logo">
  </a>

<h3 align="center">TO DO List App for Android</h3>

  <p align="center">
    Una app di esempio per introdurre alla programmazione Mobile per Android in Kotlin
    <br />
    <a href="https://github.com/falistra/todolist"><strong>Espolara la documentazione »</strong></a>
    <br />
    <br />
    <a href="https://github.com/falistra/todolist">Demo</a>
    ·
    <a href="https://github.com/falistra/todolist/issues">Report Bug</a>
    ·
    <a href="https://github.com/falistra/todolist/issues">Desiderata</a>
  </p>
</div>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Indice</summary>
  <ol>
    <li>
      <a href="#about-the-project">Il progetto</a>
      <ul>
        <li><a href="#built-with">Costruito con</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Avvio</a>
      <ul>
        <li><a href="#prerequisites">Prerequisiti</a></li>
        <li><a href="#installation">Installazione</a></li>
      </ul>
    </li>
    <li><a href="#usage">Uso</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">Licenza</a></li>
    <li><a href="#contact">Contatti</a></li>
    <li><a href="#acknowledgments">Riconoscimenti</a></li>
  </ol>
</details>


<!-- ABOUT THE PROJECT -->
## Introduzione

Questa è la mia prima mobile app, usando Android Studio - linguaggio Kotlin.
E' una ennesima variazione della app "TO DO LIST" per gestire una lista di promemoria di "cose da fare".
Ma il vero scopo è quello di esemplificare alcuni aspetti della programmazione mobile:

  <ol>
    <li> Intent espliciti e/o espliciti : p.e. Source: <b>MainActivity.kt</b>  <br>
    <pre>
    <code>    
        val bundle = Bundle()
        for (key in data.keys) {
            bundle.putSerializable(key, data.get(key))
        }
        val intent = Intent(this, ItemInfo::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    </code>
    </pre>
        Target: <b>ItemInfo.kt</b> <br>
    <pre>    
<code>
        setContentView(R.layout.activity_item_info)
        val extras : Bundle? = getIntent().getExtras()
        val what = extras?.getString("itemDataText")
</code>
    </pre>
</li>

<li>
class Kotlin per strutturare i dati. P.e. la classe ToDoModel
    <pre>    
<code>
class ToDoModel {
    companion object Factory {
        fun createList() : ToDoModel = ToDoModel()
    }
    var UID : String? = null
    var itemDataText: String? = null
    ...
    var done: Boolean? = false
}
</code>
    </pre>
</li>


<li> Interfaccia a base dati Firebase : p.e. <b>MainActivity.kt</b>  <br>
    <pre>
    <code>    
override fun onItemInfo(itemUID: String) {
    database.child("todo").child(itemUID).get()
        .addOnSuccessListener {
            Log.i("firebase", "Ricevuto valore ${it.child("itemDataText")}")
            val data: Map &lt; String, String &gt; = it.getValue() as HashMap &lt; String, String &gt;
            val bundle = Bundle()
            ...
        }
        .addOnFailureListener{
            Log.e("firebase", "Errore nel ricevere i dati", it)
        }
}
    </code>
    </pre>
</li>

<li>
Usata la Code Convention Standard di Java (pagine 9,10 e 11) del documento in
<a href="http://www.oracle.com/technetwork/java/javase/documentation/codeconvtoc-136057.html">
href="http://www.oracle.com/technetwork/java/javase/documentation/codeconvtoc-136057.html
</a>
</li>

  </ol>


<p align="right">(<a href="#top">back to top</a>)</p>



### Costruito con

L'IDE di sviluppo è : 
<pre>
Android Studio Chipmunk | 2021.2.1 Patch 1
Build #AI-212.5712.43.2112.8609683, built on May 18, 2022
Runtime version: 11.0.12+7-b1504.28-7817840 amd64
VM: OpenJDK 64-Bit Server VM by Oracle Corporation
Windows 10 10.0
GC: G1 Young Generation, G1 Old Generation
Memory: 1280M
Cores: 8
Registry: external.system.auto.import.disabled=true
Non-Bundled Plugins: org.jetbrains.kotlin (212-1.7.0-release-281-AS5457.46)
</pre>

L'IDE può essere scaricato qui:
* [Android Studio](https://developer.android.com/studio)

<p align="right">(<a href="#top">back to top</a>)</p>


<!-- GETTING STARTED -->
## Avvio 

Per ottenere una copia locale attiva e funzionante, 
seguire questi semplici passaggi.

### Installazione

1. Avviare Android Studio

2. Click su <B> Get from VCS </B>
   <img src="images/AS_1.JPG" alt="Logo">

3. in <B>HTTP</B> scrivere  <B>https://github.com/falistra/todolist</B>
   <img src="images/AS_1.JPG" alt="Logo">
4. Clonare il repo


<p align="right">(<a href="#top">back to top</a>)</p>


<!-- USAGE EXAMPLES -->
## Utilizzo

Use this space to show useful examples of how a project can be used. Additional screenshots, code examples and demos work well in this space. You may also link to more resources.

_For more examples, please refer to the [Documentation](https://example.com)_

<p align="right">(<a href="#top">back to top</a>)</p>



<!-- ROADMAP -->
## Sviluppi successivi

- [x] Add Changelog
- [x] Add back to top links
- [ ] Add Additional Templates w/ Examples
- [ ] Add "components" document to easily copy & paste sections of the readme
- [x] Supporto multilingue 
    - [x] Italiano
    - [x] Inglese

See the [open issues](https://github.com/othneildrew/Best-README-Template/issues) for a full list of proposed features (and known issues).

<p align="right">(<a href="#top">back to top</a>)</p>



<!-- CONTRIBUTING -->
## Contributi

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<p align="right">(<a href="#top">back to top</a>)</p>



<!-- LICENSE -->
## Licenza

Distributo sotto licenza MIT License.
Si declina ogni responsabilità nel suo utilizzo.

<p align="right">(<a href="#top">back to top</a>)</p>



<!-- CONTACT -->
## Contatti

Fausto Zanasi - fausto.zanasi@gmail.com

Link al progetto: [https://github.com/falistra/todolist](https://github.com/falistra/todolist)

<p align="right">(<a href="#top">back to top</a>)</p>



<!-- ACKNOWLEDGMENTS -->
## Riconoscimenti

* [Choose an Open Source License](https://choosealicense.com)
* [GitHub Emoji Cheat Sheet](https://www.webpagefx.com/tools/emoji-cheat-sheet)
* [Img Shields](https://shields.io)
* [GitHub Pages](https://pages.github.com)
* [Font Awesome](https://fontawesome.com)
* [React Icons](https://react-icons.github.io/react-icons/search)

<p align="right">(<a href="#top">back to top</a>)</p>



<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/othneildrew/Best-README-Template.svg?style=for-the-badge
[contributors-url]: https://github.com/othneildrew/Best-README-Template/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/othneildrew/Best-README-Template.svg?style=for-the-badge
[forks-url]: https://github.com/othneildrew/Best-README-Template/network/members
[stars-shield]: https://img.shields.io/github/stars/othneildrew/Best-README-Template.svg?style=for-the-badge
[stars-url]: https://github.com/othneildrew/Best-README-Template/stargazers
[issues-shield]: https://img.shields.io/github/issues/othneildrew/Best-README-Template.svg?style=for-the-badge
[issues-url]: https://github.com/othneildrew/Best-README-Template/issues
[license-shield]: https://img.shields.io/github/license/othneildrew/Best-README-Template.svg?style=for-the-badge
[license-url]: https://github.com/othneildrew/Best-README-Template/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/othneildrew
