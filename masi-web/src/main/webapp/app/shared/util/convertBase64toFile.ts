export const convertBase64toFile = (base64: string, fileName: string, fileType: string): File => {
  return new File([Uint8Array.from(atob(base64), c => c.charCodeAt(0))], fileName, { type: fileType });
};
