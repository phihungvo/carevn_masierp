import Flex from 'app/components/flex/flex';
import InputFile from 'app/components/input/input-file';
import { FILE_UTIL, ICON_PATH } from 'app/constants/common';
import useFile from 'app/hooks/use-file';
import { IFIle } from 'app/shared/model/file.model';
import { CompanySchema } from 'app/validation/company.validation';
import { useRef } from 'react';
import { useFormContext } from 'react-hook-form';

const iconUpload = () => {
  return (
    <svg
      width="40"
      height="40"
      viewBox="0 0 40 40"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
    >
      <g filter="url(#filter0_i_3511_31647)">
        <rect width="40" height="40" rx="8" fill="white" />
        <rect
          x="0.5"
          y="0.5"
          width="39"
          height="39"
          rx="7.5"
          stroke="#98A2B3"
        />
        <path
          d="M16.668 23.3333L20.0013 20M20.0013 20L23.3346 23.3333M20.0013 20V27.5M26.668 23.9524C27.6859 23.1117 28.3346 21.8399 28.3346 20.4167C28.3346 17.8854 26.2826 15.8333 23.7513 15.8333C23.5692 15.8333 23.3989 15.7383 23.3064 15.5814C22.2197 13.7374 20.2133 12.5 17.918 12.5C14.4662 12.5 11.668 15.2982 11.668 18.75C11.668 20.4718 12.3642 22.0309 13.4904 23.1613"
          stroke="#475467"
          stroke-width="1.66667"
          stroke-linecap="round"
          stroke-linejoin="round"
        />
      </g>
      <defs>
        <filter
          id="filter0_i_3511_31647"
          x="0"
          y="0"
          width="40"
          height="41"
          filterUnits="userSpaceOnUse"
          color-interpolation-filters="sRGB"
        >
          <feFlood flood-opacity="0" result="BackgroundImageFix" />
          <feBlend
            mode="normal"
            in="SourceGraphic"
            in2="BackgroundImageFix"
            result="shape"
          />
          <feColorMatrix
            in="SourceAlpha"
            type="matrix"
            values="0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 127 0"
            result="hardAlpha"
          />
          <feOffset dy="1" />
          <feGaussianBlur stdDeviation="1" />
          <feComposite in2="hardAlpha" operator="arithmetic" k2="-1" k3="1" />
          <feColorMatrix
            type="matrix"
            values="0 0 0 0 0.0627451 0 0 0 0 0.0941176 0 0 0 0 0.156863 0 0 0 0.05 0"
          />
          <feBlend
            mode="normal"
            in2="shape"
            result="effect1_innerShadow_3511_31647"
          />
        </filter>
      </defs>
    </svg>
  );
};

interface UploadLogoCompanyProps {
  name: 'imageId';
}

const { usePostFile } = useFile;

const UploadLogoCompany = ({ name }: UploadLogoCompanyProps) => {
  const { watch, setValue } = useFormContext<CompanySchema>();
  const logoId = watch(name);

  const onChangeFile = (file: IFIle) => {
    if (file) {
      setValue(name, file?.id);
    }
  };

  const fileInputRef = useRef<HTMLInputElement>(null);
  const { mutate: uploadFile } = usePostFile(onChangeFile);

  return (
    <Flex gap={20} style={{ height: '90px' }}>
      <div style={{ borderRadius: '8px', border: '1px solid #98A2B3' }}>
        {logoId ? (
          <img
            src={`${FILE_UTIL}/${logoId}`}
            style={{ width: '90px', height: '90px', borderRadius: '8px' }}
          />
        ) : (
          <img
            src={`${ICON_PATH}/img-logo-sample.svg`}
            style={{ width: '90px', height: '90px', borderRadius: '8px' }}
          />
        )}
      </div>
      <Flex
        align="center"
        direction="column"
        style={{
          width: '316px',
          borderRadius: '8px',
          padding: '12px',
          border: '1px solid #98A2B3',
        }}
        gap={6}
      >
        <div
          style={{ cursor: 'pointer' }}
          onClick={() => fileInputRef.current?.click()}
        >
          {iconUpload()}
          <InputFile
            onFileChange={file => uploadFile(file)}
            name="fileAttachment"
            hidden
            ref={fileInputRef}
          />
        </div>
        <b style={{ fontSize: '14px', lineHeight: '20px' }}>Chọn để tải lên</b>
      </Flex>
    </Flex>
  );
};

export default UploadLogoCompany;
