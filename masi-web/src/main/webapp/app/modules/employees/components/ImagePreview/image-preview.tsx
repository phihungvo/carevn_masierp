import React from 'react';
import './image-preview.scss';
import { Card } from 'reactstrap';
import { BrowserViewableExtensions } from 'app/constants/file-mime-type';
import { FILE_UTIL } from 'app/constants/common';

interface IImagePreview {
    name: string;
    onClose?: () => void;
    onClick?: () => void;
    fileUrl?: string;
    id?: string;
}

const ImagePreview = (props: IImagePreview) => {
    const { name, onClose, onClick, fileUrl, id } = props;

    const fileExtension = name?.split('.')?.pop()?.toLocaleLowerCase();

    const isSupportedFile = React.useMemo(() => {
        return BrowserViewableExtensions.includes(fileExtension);
    }, [fileExtension]);


    if (!name) {
        return null;
    }

    return (
        <Card body className='image-preview'>
            <img className='image' src={`${FILE_UTIL}/${id}`} />

            <div className='image-preview-actions'>
                {isSupportedFile &&
                    <a className="image-preview-actions-item" href={`${fileUrl}?download=false`} target="_blank"  >
                        <img src="content/images/vuesax/linear/eye-selected.svg" />
                    </a>
                }
                <a className="image-preview-actions-item" href={`${fileUrl}?download=true`} >
                    <img src="content/images/vuesax/linear/document-download-selected.svg" />
                </a>
                {onClose && (
                    <a
                        className="image-preview-actions-item"
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

export default ImagePreview;
