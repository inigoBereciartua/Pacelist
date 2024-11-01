const { defineConfig } = require('@vue/cli-service');

module.exports = defineConfig({
  transpileDependencies: true,
  pages: {
    index: {
      entry: 'src/main.js', // Adjust if main entry is different
      title: 'Sportify',
    },
  },
});

