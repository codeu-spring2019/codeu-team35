/*
 * Copyright 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Get ?user=XYZ parameter value
const urlParams = new URLSearchParams(window.location.search)
const parameterUsername = urlParams.get("user")

// URL must include ?user=XYZ parameter. If not, redirect to homepage.
if (!parameterUsername) {
  window.location.replace("/")
}

/** Sets the page title based on the URL parameter username. */
function setPageTitle() {
  document.getElementById("page-title").innerText = parameterUsername
  document.title = parameterUsername + " - User Page"
}

/**
 * Shows the message form if the user is logged in and viewing their own page.
 */
function showMessageFormIfLoggedIn() {
  fetch("/login-status")
    .then(response => {
      return response.json()
    })
    .then(loginStatus => {
      if (loginStatus.isLoggedIn) {
        fetchImageUploadUrlAndShowForm()
      }
    })
  document.getElementById("about-me-form").classList.remove("hidden")
}

function fetchImageUploadUrlAndShowForm() {
  fetch("/image-upload-url")
    .then(response => {
      return response.text()
    })
    .then(imageUploadUrl => {
      console.log("image upload url!")
      console.log(imageUploadUrl)
      const messageForm = document.getElementById("message-form")
      messageForm.action = imageUploadUrl
      messageForm.classList.remove("hidden")
      document.getElementById("recipientInput").value = parameterUsername
    })
}

/** Fetches messages and add them to the page. */
function fetchMessages() {
  const url = "/messages?user=" + parameterUsername
  fetch(url)
    .then(response => {
      return response.json()
    })
    .then(messages => {
      const messagesContainer = document.getElementById("message-container")
      if (messages.length == 0) {
        messagesContainer.innerHTML = "<p>This user has no posts yet.</p>"
      } else {
        messagesContainer.innerHTML = ""
      }
      messages.forEach(message => {
        const messageDiv = buildMessageDiv(message)
        messagesContainer.appendChild(messageDiv)
      })
    })
}

/**
 * Fetch about me string and inserts string to about me container.
 */
function fetchAboutMe() {
  const url = "/about?user=" + parameterUsername
  fetch(url)
    .then(response => {
      return response.text()
    })
    .then(aboutMe => {
      const aboutMeContainer = document.getElementById("about-me-container")
      if (aboutMe == "") {
        aboutMe = "This user has not entered any information yet."
      }

      aboutMeContainer.innerHTML = aboutMe
    })
}

/**
 * Builds an element that displays the message.
 * @param {Message} message
 * @return {Element}
 */
/*
function buildMessageDiv(message) {
  const headerDiv = document.createElement('div');
  headerDiv.classList.add('message-header');
  headerDiv.appendChild(document.createTextNode(
      message.user + ' - ' + new Date(message.timestamp)));

  const bodyDiv = document.createElement('div');
  bodyDiv.classList.add('message-body');
  bodyDiv.innerHTML = message.text;
  if(message.imageUrl){
	bodyDiv.innerHTML += '<br/>';
  	bodyDiv.innerHTML += '<img src="' + message.imageUrl + '" />';
  }

  const messageDiv = document.createElement('div');
  messageDiv.classList.add('message-div');
  messageDiv.appendChild(headerDiv);
  messageDiv.appendChild(bodyDiv);

  return messageDiv;
}
*/

function buildMessageDiv(message) {
  const headerDiv = document.createElement("div")
  headerDiv.classList.add("message-header")
  headerDiv.classList.add("padded")

  /*
  headerDiv.appendChild(document.createTextNode(
      message.user + ' - ' + formatDate(message.timestamp)));
*/
  headerDiv.appendChild(
    document.createTextNode(message.user + " - " + new Date(message.timestamp))
  )

  const bodyDiv = document.createElement("div")
  bodyDiv.classList.add("message-body")
  bodyDiv.classList.add("padded")
  bodyDiv.innerHTML = message.text

  const messageDiv = document.createElement("div")
  messageDiv.classList.add("message-div")
  messageDiv.classList.add("rounded")
  messageDiv.classList.add("panel")
  messageDiv.appendChild(headerDiv)
  messageDiv.appendChild(bodyDiv)

  return messageDiv
}
/** Fetches data and populates the UI of the page. */
function buildUI() {
  setPageTitle()
  showMessageFormIfLoggedIn()
  fetchMessages()
  fetchAboutMe()
  ClassicEditor.create(document.getElementById("message-input"))
}
