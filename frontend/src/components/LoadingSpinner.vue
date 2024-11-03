<template>
    <div class="container" v-if="isLoading">
        <div class="spinner">
            <div class="spinner-circle"></div>
        </div>
        <p class="spinner-text">{{ text }}</p>
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
        text: {
            type: String,
            default: "Loading...",
        }
    },
};
</script>

<style scoped>

.container {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    height: 100vh;
}

  .spinner-circle {
    border: 5px solid rgba(0, 0, 0, 0.1); /* Light outer border */
    border-top: 5px solid #1db954; /* Green color for the spinner (Spotify-like color) */
    border-radius: 50%;
    width: 50px;
    height: 50px;
    animation: spin 1s linear infinite;
}

.spinner-text {
    margin-top: 10px;
    color: #b3b3b3; /* Subtle gray for text */
    font-size: 1em;
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
