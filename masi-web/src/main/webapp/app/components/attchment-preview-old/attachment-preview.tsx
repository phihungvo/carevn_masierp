import { shortenFileName } from 'app/shared/util/shorten-text';
import React from 'react';
import Flex from '../flex/flex';
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

  return (
    <Card body style={{
      display: 'flex',
      justifyContent: 'space-between',
      alignItems: 'center',
      flexDirection: 'row',
      maxWidth: '300px',
      height: '64px',
      padding: '12px',
    }}>

      <Flex align="center" gap={8} onClick={onClick}>
        <span />
        <p>{shortenFileName(name)}</p>
      </Flex>
      <div>
        {isSupportedFile &&
          <a href={`${fileUrl}?download=false`} target="_blank"  >
            <img src="content/images/vuesax/linear/eye.svg" />
          </a>}
        <a href={`${fileUrl}?download=true`} >
          <img src="content/images/vuesax/linear/document-download.svg" />
        </a>
      </div>
    </Card>
    // <div className="preview preview-bg">
    //   <div className="preview-actions">
    //     <Flex gap={4}>
    //       {
    //         isSupportedFile &&
    //         <a href={`${fileUrl}?download=false`} target="_blank" className="preview-action-item" >
    //           <img src="content/images/vuesax/linear/eye.svg" />
    //         </a>
    //       }
    //       <a href={`${fileUrl}?download=true`} className="preview-action-item">
    //         <img src="content/images/vuesax/linear/document-download.svg" />
    //       </a>
    //       {onClose && (
    //         <span
    //           className="preview-action-item"
    //           onClick={e => {
    //             e.stopPropagation();
    //             onClose();
    //           }}
    //         >
    //           <img src="content/images/vuesax/linear/close-normal.svg" />
    //         </span>
    //       )}
    //     </Flex>
    //   </div>

    //   <a href="content/images/vuesax/linear/paperclip.svg" target="_blank" />
    //   <Flex align="center" gap={8} className="preview-text" onClick={onClick}>
    //     <span />
    //     <p>{shortenFileName(name)}</p>
    //   </Flex>
    // </div>
  );
};

export default AttachmentPreview;
