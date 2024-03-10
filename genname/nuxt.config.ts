export default defineNuxtConfig({
  modules: ['@nuxt/ui'],
  ui: {
    global: true,
    icons: ['mdi', 'simple-icons', 'heroicons']
  },
  colorMode: {
    preference: 'light',
  },
})
