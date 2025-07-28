import { shortenFileName } from 'app/shared/util/shorten-text';
import React from 'react';
import Flex from '../flex/flex';
import './attachment-preview.scss';
import { BrowserViewableExtensions } from 'app/constants/file-mime-type';
import { Card } from 'reactstrap';
import Tooltip from '../tooltip/tooltip';

interface IAttachmentPreviewV2 {
  name: string;
  onClose?: () => void;
  onClick?: () => void;
  fileUrl?: string;
}

const AttachmentPreviewV2 = (props: IAttachmentPreviewV2) => {
  const { name, onClose, onClick, fileUrl } = props;

  const fileExtension = name?.split('.')?.pop()?.toLocaleLowerCase();

  const isSupportedFile = React.useMemo(() => {
    return BrowserViewableExtensions.includes(fileExtension);
  }, [fileExtension]);

  if (!name) {
    return null;
  }

  return (
    <div className="preview-v2">
      <Flex className="preview-v2-text" gap={8} onClick={onClick}>
        <Tooltip label={name} target="name">
          <p className="title-file" id="name">
            {shortenFileName(name)}
          </p>
        </Tooltip>
      </Flex>
      <div className="preview-v2-actions">
        {isSupportedFile && (
          <a
            className="preview-v2-actions-item"
            href={`${fileUrl}?download=false`}
            target="_blank"
            rel="noreferrer"
          >
            <img src="content/images/vuesax/linear/eye-selected.svg" />
          </a>
        )}
        <a
          className="preview-v2-actions-item"
          href={`${fileUrl}?download=true`}
        >
          <img src="content/images/vuesax/linear/document-download-selected.svg" />
        </a>
        {onClose && (
          <a
            className="preview-v2-actions-item"
            onClick={e => {
              e.stopPropagation();
              onClose();
            }}
          >
            <img src="content/images/vuesax/linear/trash-selected.svg" />
          </a>
        )}
      </div>
    </div>
  );
};

export default AttachmentPreviewV2;
