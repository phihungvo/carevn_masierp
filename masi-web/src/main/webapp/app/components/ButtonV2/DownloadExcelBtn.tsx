import { iconPath } from 'app/shared/util/format';
import { ComponentProps } from 'react';
import ButtonV2 from './ButtonV2';
import { useExportExcel } from 'app/hooks/use-export-excel';

type Props = Omit<ComponentProps<typeof ButtonV2>, 'left_section'> & Parameters<typeof useExportExcel>[0];

const DownloadExcelBtn = (props: Props) => {
  const { axiosFn, fileName, fileType, key } = props

  const trigger = useExportExcel({ axiosFn, fileName, fileType, key });

  return (
    <ButtonV2
      {...props}
      left_section={<img src={iconPath('export-excel.svg')} alt="excel" />}
      onClick={trigger}
    >
      Excel
    </ButtonV2>
  );
};

export default DownloadExcelBtn;
