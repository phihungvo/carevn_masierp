export interface IFIle {
  id: string;
  name?: string;
  path?: string;
  fileSize?: string;
  mimeType?: string;
  base64?: string;
  type?: string;
  createdAt?: string;
}

export interface IBodyFile {
  id: string;
  fileName: string;
}
