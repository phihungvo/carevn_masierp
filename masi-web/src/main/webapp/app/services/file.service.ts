import { fileEndpoints } from 'app/constants/endpoints';
import { IFIle } from 'app/shared/model/file.model';
import axios from 'axios';

const postFile = async (files: File) => {
  const url = fileEndpoints.postFile;

  const formData = new FormData();
  formData.append('files', files);

  return await axios.post<IFIle>(url, formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
};

export default {
  postFile,
};
