<template>
    <div class="spinner" v-if="isLoading">
      <div class="spinner-circle"></div>
    </div>
  </template>
  
  <script>
  export default {
    name: "LoadingSpinner",
    props: {
      isLoading: {
        type: Boolean,
        required: true,
      },
    },
  };
  </script>
  
  <style scoped>
  .spinner {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100px; /* Set the height you want */
  }
  
  .spinner-circle {
    border: 5px solid rgba(0, 0, 0, 0.1); /* Light outer border */
    border-top: 5px solid #1db954; /* Green color for the spinner (Spotify-like color) */
    border-radius: 50%;
    width: 50px;
    height: 50px;
    animation: spin 1s linear infinite;
  }
  
  @keyframes spin {
    0% {
      transform: rotate(0deg);
    }
    100% {
      transform: rotate(360deg);
    }
  }
  </style>
  