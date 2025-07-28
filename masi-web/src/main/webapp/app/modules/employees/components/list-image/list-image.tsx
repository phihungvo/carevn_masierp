import { ImageFormats } from 'app/constants/file-mime-type';
import React from 'react'
import ImagePreview from '../ImagePreview/image-preview';
import { FILE_UTIL } from 'app/constants/common';
import { shortenFileName } from 'app/shared/util/shorten-text';
import AttachmentPreview from '../attachment-preview/attachment-preview';
import { EProfileFile, IProfileFile } from '../../modals/employee-upload-modal';
import Tooltip from 'app/components/tooltip/tooltip';

interface IListImage {
    name?: EProfileFile;
    fileProfile: IProfileFile
    setFileProfile: React.Dispatch<React.SetStateAction<IProfileFile>>
}

function ListImage({ name, fileProfile, setFileProfile }: IListImage) {
    return (
        <div className='list-image'>
            {
                fileProfile?.[name]?.map(item => {
                    if (ImageFormats?.includes(item.name?.split('.')[1])) {
                        return item?.id && (
                            <div className='image-container' key={item.id}>
                                <ImagePreview
                                    key={item.id}
                                    name={item?.name}
                                    onClose={() => setFileProfile(prev => ({
                                        ...prev,
                                        [name]: prev?.[name]?.filter(curFile => curFile?.id !== item?.id)
                                    }))}
                                    fileUrl={`${FILE_UTIL}/${item?.id}`}
                                    id={item?.id}
                                />
                                <Tooltip label={item?.name} target={`image-name-${item?.id}`}>
                                    <div className='image-footer' id={`image-name-${item?.id}`}>
                                        {shortenFileName(item?.name)}
                                    </div>
                                </Tooltip>

                            </div>
                        );
                    } else {
                        return item?.id && (
                            <div className='image-container' key={item.id}>
                                <AttachmentPreview
                                    key={item.id}
                                    name={item?.name}
                                    onClose={() => setFileProfile(prev => ({
                                        ...prev,
                                        [name]: prev?.[name]?.filter(curFile => curFile?.id !== item?.id)
                                    }))}
                                    fileUrl={`${FILE_UTIL}/${item?.id}`}
                                />
                                <Tooltip label={item?.name} target={`image-name-${item?.id}`}>
                                    <div className='image-footer' id={`image-name-${item?.id}`}>
                                        {shortenFileName(item?.name)}
                                    </div>
                                </Tooltip>
                            </div>
                        );
                    }
                })
            }
        </div>
    )
}

export default ListImage