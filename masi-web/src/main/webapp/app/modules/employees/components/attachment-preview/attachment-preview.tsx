import React from 'react';
import './attachment-preview.scss';
import { BrowserViewableExtensions } from 'app/constants/file-mime-type';
import { Card } from 'reactstrap';

interface IAttachmentPreview {
  name: string;
  onClose?: () => void;
  onClick?: () => void;
  fileUrl?: string;
}

const AttachmentPreview = (props: IAttachmentPreview) => {
  const { name, onClose, onClick, fileUrl } = props;

  const fileExtension = name?.split('.')?.pop()?.toLocaleLowerCase();

  const isSupportedFile = React.useMemo(() => {
    return BrowserViewableExtensions.includes(fileExtension);
  }, [fileExtension]);


  if (!name) {
    return null;
  }

  return (
    <Card body className='custom-preview'>
      <p className='title'>{name?.split('.')[1]?.toLocaleUpperCase()}</p>
      <div className='custom-preview-actions'>
        {isSupportedFile &&
          <a className="custom-preview-actions-item" href={`${fileUrl}?download=false`} target="_blank"  >
            <img src="content/images/vuesax/linear/eye-selected.svg" />
          </a>
        }
        <a className="custom-preview-actions-item" href={`${fileUrl}?download=true`} >
          <img src="content/images/vuesax/linear/document-download-selected.svg" />
        </a>
        {onClose && (
          <a
            className="custom-preview-actions-item"
            onClick={e => {
              e.stopPropagation();
              onClose();
            }}
          >
            <img src="content/images/vuesax/linear/trash-selected.svg" />
          </a>
        )}
      </div>
    </Card>
  );
};

export default AttachmentPreview;
