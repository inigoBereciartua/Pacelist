<template>
    <button @click="goBack" class="back-button">←</button>
</template>

<script>

export default {
    name: 'BackArrow',
    methods: {
        goBack() {
            this.$router.go(-1);
        }
    }
};
</script>

<style>
.back-button {
    background: none;
    border: none;
    color: #1db954; /* Spotify green color */
    font-size: 24px; /* Size for visibility */
    cursor: pointer;
    padding: 0;
    margin: 0; /* Removes all margins */
    display: inline-block;
}
.back-button:hover {
    color: #1ed760; /* Lighter shade on hover */
}

</style>
