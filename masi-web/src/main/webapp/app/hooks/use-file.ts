import { useMutation } from '@tanstack/react-query';
import fileService from 'app/services/file.service';
import { IFIle } from 'app/shared/model/file.model';

const usePostFiles = (onChangeFiles?: React.Dispatch<React.SetStateAction<IFIle[]>>) => {
  return useMutation({
    mutationFn: (file: File) => fileService.postFile(file),
    onSuccess: data => {
      onChangeFiles && onChangeFiles(prev => [...prev, data?.data]);
    },
  });
};

const usePostFile = (onChangeFile?: React.Dispatch<React.SetStateAction<IFIle>>, onError?: (err: Error) => void) => {
  return useMutation({
    mutationFn: (file: File) => fileService.postFile(file),
    onSuccess: data => {
      onChangeFile && onChangeFile(data?.data);
    },
    onError
  });
};

export default {
  usePostFiles,
  usePostFile,
};
