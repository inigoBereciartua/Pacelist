import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import Toast from 'vue-toastification';
import 'vue-toastification/dist/index.css';
import axios from 'axios';

import { API_URL } from "@/api/utils";
import { tokenInterceptor } from "@/api/auth";


document.title = 'Pacelist';

const app = createApp(App);

const toastOptions = {
    position: "top-center",
    timeout: 5000,
    closeOnClick: true,
    pauseOnFocusLoss: true,
    pauseOnHover: true,
    draggable: true,
    draggablePercent: 0.6,
    showCloseButtonOnHover: false,
    hideProgressBar: true,
    closeButton: "button",
    icon: true,
    rtl: false
  }

app.use(Toast, toastOptions);

app.use(router).mount('#app');

axios.defaults.baseURL = API_URL;
app.config.globalProperties.$axios = axios
axios.interceptors.request.use(tokenInterceptor);