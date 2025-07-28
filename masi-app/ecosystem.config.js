const path = require("path");


const currentScriptPath = path.dirname(__filename);
const servicesFolder = ["employee", "production", "sale", "utility"];
const apps = servicesFolder.map((service) => {
  const servicePath = path.join(currentScriptPath, service);
  return {
    script: "bash",
    name: `masi-${service}`,
    args: `mvn -DskipTests`,
    cwd: servicePath,
  };
});
module.exports = {
  apps: apps,
};
