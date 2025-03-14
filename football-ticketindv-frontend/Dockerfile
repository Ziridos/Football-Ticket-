# Build stage
FROM node:18-alpine AS build
WORKDIR /app

# Add necessary build dependencies for image optimization packages
RUN apk add --no-cache \
    autoconf \
    automake \
    make \
    g++ \
    libtool \
    nasm \
    pkgconfig \
    git \
    zlib \
    zlib-dev \
    libpng \
    libpng-dev \
    python3

# Install dependencies first
COPY package*.json ./

# Install dependencies with production flag to skip devDependencies
RUN npm ci --production=false

# Copy source and build
COPY . .
RUN npm run build

# Production stage
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html

# Add nginx config for SPA
RUN echo 'server { \
    listen 80; \
    location / { \
        root /usr/share/nginx/html; \
        index index.html; \
        try_files $uri $uri/ /index.html; \
    } \
}' > /etc/nginx/conf.d/default.conf

EXPOSE 80