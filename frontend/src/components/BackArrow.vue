<template>
    <div class="back-button-container">
        <button @click="goBack" class="back-button">
            <svg
                xmlns="http://www.w3.org/2000/svg"
                width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                class="icon-arrow-left"
            >
                <polyline points="15 18 9 12 15 6"></polyline>
            </svg>
            <span>{{ text }}</span>
        </button>
    </div>
</template>

<script>
export default {
    name: 'BackArrow',
    props: {
        text: {
            type: String,
            default: ''
        },
        onClick: {
            type: Function,
            default: () => {}
        }
    },
    methods: {
        goBack() {
            if (this.onClick) {
                this.onClick();
            } else {
                this.$router.go(-1);
            }
        }
    }
};
</script>

<style>
.back-button-container {
    display: flex;
    align-items: center; /* Vertically centers the button within the container */
}

.back-button {
    background: none;
    border: none;
    color: #1db954; /* Spotify green color */
    cursor: pointer;
    padding: 0;
    margin: 0;
    display: inline-flex; /* Centers content within */
    align-items: center;
    justify-content: center;
}

.back-button:hover {
    color: #1ed760; /* Lighter shade on hover */
}

.icon-arrow-left {
    width: 24px;
    height: 24px;
}
</style>
