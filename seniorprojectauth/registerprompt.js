import { initializeApp } from "https://www.gstatic.com/firebasejs/9.4.0/firebase-app.js";

import {
    hideLoginError,
    showLoginState,
    showLoginForm,
    showApp,
    showLoginError,
    btnLogin,
    submit,
    btnLogout,
} from './iu';
import {
    getAuth,
    onAuthStateChanged, 
    signOut,
    createUserWithEmailAndPassword,
    signInWithEmailAndPassword,
    connectAuthEmulator
} from 'https://www.gstatic.com/firebasejs/9.0.0/firebase-auth.js';

// Your Firebase configuration
const firebaseConfig = {
    apiKey: "AIzaSyAX6PzdmeSH3zbMjTdzJ9QjSDFOr-i_I10",
    authDomain: "senior-project-2024s.firebaseapp.com",
    projectId: "senior-project-2024s",
    storageBucket: "senior-project-2024s.appspot.com",
    messagingSenderId: "92548243517",
    appId: "1:92548243517:web:b03fc1b33c0c4517d48a54",
    measurementId: "G-9275MPZELC"
};
  
  const createAccount = async () => {
    const email = txtEmail.value
    const password = txtPassword.value
  
    try {
      await createUserWithEmailAndPassword(auth, email, password)
    }
    catch(error) {
      console.log(`There was an error: ${error}`)
      showLoginError(error)
    } 
  }
  
  // Monitor auth state
  const monitorAuthState = async () => {
    onAuthStateChanged(auth, user => {
      if (user) {
        console.log(user)
        showApp()
        showLoginState(user)
  
        hideLoginError()
        hideLinkError()
      }
      else {
        showLoginForm()
        lblAuthState.innerHTML = `You're not logged in.`
      }
    })
  }
  
  // Log out
  const logout = async () => {
    await signOut(auth);
  }
  
  btnLogin.addEventListener("click", loginEmailPassword) 
  submit.addEventListener("click", createAccount)
  btnLogout.addEventListener("click", logout)
  
  const auth = getAuth(firebaseApp);
  connectAuthEmulator(auth, "http://localhost:9099");
  
  monitorAuthState();