import { imageMap } from '../assets/imageMap';

// 创建一个Map来存储预加载的图片
const preloadedImages = new Map();

// 预加载单个图片的函数
const preloadImage = (url) => {
  return new Promise((resolve, reject) => {
    const img = new Image();
    img.src = url;
    img.onload = () => {
      preloadedImages.set(url, img);
      resolve(img);
    };
    img.onerror = reject;
  });
};

// 预加载所有图片的函数
export const preloadAllImages = async () => {
  const imageUrls = Object.values(imageMap);
  try {
    await Promise.all(imageUrls.map(preloadImage));
    console.log('所有图片预加载完成');
  } catch (error) {
    console.error('图片预加载出错:', error);
  }
};

// 获取预加载的图片
export const getPreloadedImage = (imageId) => {
  const url = imageMap[imageId];
  return preloadedImages.get(url);
};

// 检查图片是否已预加载
export const isImagePreloaded = (imageId) => {
  const url = imageMap[imageId];
  return preloadedImages.has(url);
}; 