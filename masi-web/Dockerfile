FROM node:20 AS build

WORKDIR /app

COPY package.json ./
RUN npm install

COPY . .
RUN npm run build

# Stage 2: Serve with NGINX
FROM nginx:alpine

COPY --from=build /app/target/classes/static /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]