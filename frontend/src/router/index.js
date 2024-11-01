import { createRouter, createWebHistory } from 'vue-router';
import SpotifyLogin from '../layouts/SpotifyLogin.vue';
import PlaylistInfoForm from '@/layouts/PlaylistInfoForm.vue';
import PlaylistPreview from '@/layouts/PlaylistPreview.vue';

const routes = [
  {
    path: '/',
    name: 'Home',
    component: SpotifyLogin,
  },
  {
    path: '/login',
    name: 'SpotifyLogin',
    component: SpotifyLogin,
  },
  {
    path: '/playlist-info-form',
    name: 'PlaylistInfoForm',
    component: PlaylistInfoForm
  },

  {
    path: '/playlist-preview',
    name: 'PlaylistPreview',
    component: PlaylistPreview
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/login',
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;