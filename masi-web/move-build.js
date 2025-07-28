const fs = require('fs-extra'); // Ensure fs-extra is installed
const path = require('path');

const source = path.join(__dirname, 'target/classes/static/'); // Source build folder
const destination = path.join(__dirname, 'target/classes/build/'); // Target folder

async function moveBuild() {
  try {
    // Ensure the destination folder exists
    await fs.ensureDir(destination);
    console.log('Target folder ensured to exist.');

    // Clear the target folder
    await fs.emptyDir(destination);
    console.log('Target folder cleared.');
    // Move the build folder to the target location
    await fs.move(source, destination, { overwrite: true });
  } catch (err) {
    console.error('Error moving build:', err);
  }
}

moveBuild();