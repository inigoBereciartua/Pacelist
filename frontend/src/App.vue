<template>
  <div id="app">
    <router-view />  <!-- This will render the active route component -->
  </div>
</template>

<script>

export default {
  name: 'App',
};
</script>

<style>

@import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap');

body, html {
  font-family: 'Poppins', sans-serif;
  height: 100%;
  margin: 0;
  display: flex;
  justify-content: center;
  align-items: flex-start; /* Change align-items to flex-start for vertical centering */
  background-color: #121212;
  color: #fff;
  box-sizing: border-box;
  /* prevent view from "touching" bottom edge */
  padding-bottom: 20px;
}

#app {
  display: flex;
  justify-content: center;
  align-items: flex-start; /* This makes sure all content is centered horizontally */
  width: 100%;
  height: 100%;
  padding: 20px;
  box-sizing: border-box; /* Ensure padding is included in the width */
  padding-bottom: 20px;
}

button {
  background-color: #217a40;
  color: #fff;
  border: none;
  padding: 10px 20px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  margin: 20px;
}

button:hover {
  background-color: #0f973e;
}
</style>