//Login popup modal
var modal = document.getElementById("login");

var btn = document.getElementById("loginBtn");

var span = document.getElementsByClassName("close")[0];

btn.onclick = function () {
    modal.style.display = "block";
}

span.onclick = function () {
    modal.style.display = "none";
}

window.onclick = function (event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}

//----------------------------------
//Register popup modal
var registerModal = document.getElementById("register");

var registerBtn = document.getElementById("registerBtn");

var closeBtnRegister = registerModal.getElementsByClassName("close")[0];

registerBtn.onclick = function () {
    registerModal.style.display = "block";
}

closeBtnRegister.onclick = function () {
    registerModal.style.display = "none";
}

window.onclick = function (event) {
    if (event.target == registerModal) {
        registerModal.style.display = "none";
    }
}

//------------------------------------
//Show games function
function show(status) {
    var allRows = document.querySelectorAll('.game-row');
    var gamesContainer = document.querySelector('#games-list tbody');
    var firstGameRow = gamesContainer.querySelector('.game-row');

    var matchingRows = [];
    allRows.forEach(function (row) {
        if (row.dataset.status === status || status === 'all') {
            matchingRows.push(row);
        } else {
            row.classList.add('hidden');
        }
    });

    matchingRows.forEach(function (row) {
        row.classList.remove('hidden');
    });
    updateGameNumbers();
}

//-----------------------------
//firebase imports & initialization
import { initializeApp } from "https://www.gstatic.com/firebasejs/10.7.2/firebase-app.js";
import { getDatabase, get, set, ref, child } from "https://www.gstatic.com/firebasejs/10.7.2/firebase-database.js";
import { getAuth, createUserWithEmailAndPassword, signInWithEmailAndPassword } from "https://www.gstatic.com/firebasejs/10.7.2/firebase-auth.js";
const firebaseConfig = {
    apiKey: "AIzaSyAX6PzdmeSH3zbMjTdzJ9QjSDFOr-i_I10",
    authDomain: "senior-project-2024s.firebaseapp.com",
    projectId: "senior-project-2024s",
    storageBucket: "senior-project-2024s.appspot.com",
    messagingSenderId: "92548243517",
    appId: "1:92548243517:web:b03fc1b33c0c4517d48a54",
    measurementId: "G-9275MPZELC"
};
const app = initializeApp(firebaseConfig);
const db = getDatabase();
const auth = getAuth(app);
const dbref = ref(db);

let EmailInp = document.getElementById('emailInp');
let PassInp = document.getElementById('passwordInp');
let FnameInp = document.getElementById('fnameInp');
let LnameInp = document.getElementById('lnameInp');
let loginForm = document.getElementById('login');
let registerForm = document.getElementById('register');

//-----------------------------
//registerprompt.js file

let RegisterUser = evt => {
    evt.preventDefault();

    createUserWithEmailAndPassword(auth, EmailInp.value, PassInp.value)
        .then((credentials) => {
            set(ref(db, 'UsersAuthList/' + credentials.user.uid), {
                firstname: FnameInp.value,
                lastname: LnameInp.value
            })
            console.log(credentials);
        })
        .catch((error) => {
            alert(error.message);
            console.log(error.code);
            console.log(error.message);
        })
}

registerForm.addEventListener('submit', RegisterUser)

//----------------------------------------
//loginprompt.js file

let SignInUser = evt => {
    evt.preventDefault();

    signInWithEmailAndPassword(auth, EmailInp.value, PassInp.value)
        .then((credentials) => {
            get(child(dbref, 'UsersAuthList/' + credentials.user.uid)).then((snapshot) => {
                if (snapshot.exists) {
                    sessionStorage.setItem("user-info", JSON.stringify({
                        firstname: snapshot.val().firstname,
                        lastname: snapshot.val().lastname
                    }))
                    sessionStorage.setItem("user-creds", JSON.stringify(credentials.user));
                    window.location.href = 'home.html'
                }
            })
            console.log(credentials);
        })
        .catch((error) => {
            alert(error.message);
            console.log(error.code);
            console.log(error.message);
        })
}

loginForm.addEventListener('submit', SignInUser)

//---------------------------------
//index.js file

let UserCreds = JSON.parse(sessionStorage.getItem("user-cred"));
let UserInfo = JSON.parse(sessionStorage.getItem("user-info"));

let MsgHead = document.getElementById('msg');
let GreetHead = document.getElementById('greet');
let SignoutBtn = document.getElementById('signoutbutton')



let Signout = () => {
    sessionStorage.removeItem("user-cred");
    sessionStorage.removeItem("user-info");
    window.location.href = 'login.html'
}

let CheckCred = () => {
    if (!sessionStorage.getItem("user-cred"))
        window.location.href = 'loginprompt.html'

    else {
        MsgHead.innerText = `user with email "${UserCreds.email}" logged in`;
        GreetHead.innerText = `welcome ${userInfo.firstname + " " + UserInfo.lastname}!`;
    }
}


//window.addEventListener('load', CheckCred);
//SignoutBtn.addEventListener('click', Signout);
