import axios from 'axios';
import React, { SetStateAction, forwardRef } from 'react';

interface IIputFile extends React.HTMLProps<HTMLInputElement> {
  action?: string; // Uploading URL
  onFileChange?: (file: File) => void;
  onFileListChange?: React.Dispatch<SetStateAction<File[]>>;
}

const InputFile = forwardRef<HTMLInputElement, IIputFile>((props, ref) => {
  const { action, onFileChange, onFileListChange, ...rest } = props;

  const onChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const fileUpload = e.target.files?.[0];

    if (fileUpload) {
      onFileChange && onFileChange(fileUpload);
      onFileListChange && onFileListChange(prev => [...prev, e.target.files?.[0]]);
      action && (await axios.post(action || '', fileUpload));
    }
  };

  return <input {...rest} type="file" ref={ref} onChange={onChange} />;
});

export default InputFile;

export function convertFileToBase64(file: File): Promise<string> {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => {
      let encoded = reader.result.toString().replace('data:', '').replace(/^.+,/, '');
      if (encoded.length % 4 > 0) {
        encoded += '='.repeat(4 - (encoded.length % 4));
      }
      resolve(encoded);
    };
    reader.onerror = error => reject(error);
  });
}

export function downloadBase64File(contentBase64: string, fileName: string, fileType: string) {
  const linkSource = `data:${fileType};base64,${contentBase64}`;
  const downloadLink = document.createElement('a');
  document.body.appendChild(downloadLink);

  downloadLink.href = linkSource;
  downloadLink.target = '_self';
  downloadLink.download = fileName;
  downloadLink.click();
}
