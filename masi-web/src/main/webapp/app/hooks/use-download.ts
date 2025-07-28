import { convertFileToBase64 } from 'app/components/input/input-file';
import { useEffect } from 'react';
import { utils, writeFile } from 'xlsx';

export const useDownload = (data: string, fileName: string, extension: string) => {
  useEffect(() => {
    if (data) {
      // Create blob link to download
      // Convert CSV data to JSON
      const lines = data.trim().split('\n');
      const headers = lines[0].split(',');
      const dataCsv = lines.slice(1).map(line => {
        const values = line.split(',');
        return headers.reduce((object, header, index) => {
          object[header?.replace(/\"/g, '')] = values[index]?.replace(/\"/g, '');
          return object;
        }, {});
      });

      // Create a new workbook and a sheet
      const wb = utils.book_new();
      const ws = utils.json_to_sheet(dataCsv);

      // Append the sheet to the workbook
      utils.book_append_sheet(wb, ws, 'Sheet1');

      // Generate Excel file and trigger download
      writeFile(wb, `${fileName}.${extension}`);

      // const url = window.URL.createObjectURL(new Blob([data], { type: 'text/csv;charset=utf-8' }));
      // const link = document.createElement('a');
      // link.href = url;
      // link.setAttribute('download', `${fileName}.${extension}`);

      // // Append to html link element page
      // document.body.appendChild(link);

      // // Start download
      // link.click();

      // // Clean up and remove the link
      // link.parentNode.removeChild(link);
    }
  }, [data]);
};

export const useDownloadBase64 = (data: string, filePath: string) => {
  useEffect(() => {
    if (data) {
      const url = 'data:image/png;base64,' + data;
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', filePath);

      // Append to html link element page
      document.body.appendChild(link);

      // Start download
      link.click();

      // Clean up and remove the link
      link.parentNode.removeChild(link);
    }
  }, [data]);
};

export const handleDownloadBase64 = async (file: File, filePath: string) => {
  if (file) {
    const base64File = await convertFileToBase64(file);
    const url = 'data:image/png;base64,' + base64File;
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', filePath);

    // Append to html link element page
    document.body.appendChild(link);

    // Start download
    link.click();

    // Clean up and remove the link
    link.parentNode.removeChild(link);
  }
};

export function dataURLtoFile(dataurl: string, filename: string) {
  var arr = dataurl.split(','),
    mime = arr[0].match(/:(.*?);/)[1],
    bstr = atob(arr[arr.length - 1]),
    n = bstr.length,
    u8arr = new Uint8Array(n);
  while (n--) {
    u8arr[n] = bstr.charCodeAt(n);
  }
  return new File([u8arr], filename, { type: mime });
}

export const useDownloadDocx = (data: string, fileName: string, extension: string) => {
  useEffect(() => {
    if (data) {
      // Create blob link to download
      const url = window.URL.createObjectURL(new Blob([data], { type: 'application/octet-stream' }));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `${fileName}.${extension}`);

      // Append to html link element page
      document.body.appendChild(link);

      // Start download
      link.click();

      // Clean up and remove the link
      link.parentNode.removeChild(link);
    }
  }, [data]);
};

export const useDownloadXlsx = (data: string, fileName: string, extension: string) => {
  useEffect(() => {
    if (data) {
      // Create blob link to download
      const url = window.URL.createObjectURL(new Blob([data], { type: 'application/octet-stream' }));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `${fileName}.${extension}`);

      // Append to html link element page
      document.body.appendChild(link);

      // Start download
      link.click();

      // Clean up and remove the link
      link.parentNode.removeChild(link);
    }
  }, [data]);
};

export const useDownloadPdf = (data: string, fileName: string, extension: string) => {
  useEffect(() => {
    if (data) {
      // Create blob link to download
      const url = window.URL.createObjectURL(new Blob([data], { type: 'application/octet-stream' }));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `${fileName}.${extension}`);

      // Append to html link element page
      document.body.appendChild(link);

      // Start download
      link.click();

      // Clean up and remove the link
      link.parentNode.removeChild(link);
    }
  }, [data]);
};
