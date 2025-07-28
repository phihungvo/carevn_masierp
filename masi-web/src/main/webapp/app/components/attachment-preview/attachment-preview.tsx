import { shortenFileName } from 'app/shared/util/shorten-text';
import React from 'react';
import Flex from '../flex/flex';
import './attachment-preview.scss';
import { BrowserViewableExtensions } from 'app/constants/file-mime-type';
import { Card } from 'reactstrap';
import Tooltip from '../tooltip/tooltip';

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
    <Card body className='preview'>
      <Flex align="center" className="preview-text" gap={8} onClick={onClick}>
        <span />
        <Tooltip label={name} target='name'>
          <p className='title-file' id='name'>{shortenFileName(name)}</p>
        </Tooltip>
      </Flex>
      <div className='preview-actions'>
        {isSupportedFile &&
          <a className="preview-actions-item" href={`${fileUrl}?download=false`} target="_blank"  >
            <img src="content/images/vuesax/linear/eye-selected.svg" />
          </a>
        }
        <a className="preview-actions-item" href={`${fileUrl}?download=true`} >
          <img src="content/images/vuesax/linear/document-download-selected.svg" />
        </a>
        {onClose && (
          <a
            className="preview-actions-item"
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
