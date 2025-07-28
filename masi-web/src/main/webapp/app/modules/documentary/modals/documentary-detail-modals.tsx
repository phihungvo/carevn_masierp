import Flex from 'app/components/flex/flex';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { DATE_FORMAT, FILE_UTIL } from 'app/constants/common';
import dayjs from 'dayjs';
import React, { useEffect, useState } from 'react';
import AttachmentPreview from 'app/components/attachment-preview/attachment-preview';
import { useLocation } from 'react-router';
import documentaryMapping from '../documentary-mapping';
import useDocumentary from 'app/hooks/use-documentary';
import Descriptions from '@uiw/react-descriptions';
import { Row } from 'reactstrap';
import { IBodyFile } from 'app/shared/model/file.model';

const { documentaryGroupMapping, documentaryTypeMapping } = documentaryMapping;
const { useGetDocumentaryByIdQuery } = useDocumentary;

interface IDocumentaryDetailModalsProps {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord?: string | null;
}

const DocumentaryDetailModals = (props: IDocumentaryDetailModalsProps) => {
  const { isOpen, toggle, selectedRecord } = props;

  const { search } = useLocation();
  const idQuery = search?.split('=')[1];

  const [file, setFile] = useState<File>();

  const { data } = useGetDocumentaryByIdQuery(idQuery || selectedRecord);

  useEffect(() => {
    if (idQuery) {
      toggle();
    }
  }, [idQuery]);

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      ok={false}
      className="modals-detail-documentary"
      titleHeader='Chi tiết công văn'
    >
      <Flex direction="column" gap={16}>
        <Descriptions title="Thông tin chung" size="large" bordered column={2}>
          <Descriptions.Item label="Số công văn">
            <Typography level="text" className="fw-bolder">
              {data?.documentNumber}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Ngày">
            <Typography level="text" className="fw-bolder">
              {data?.dateStart ? dayjs(data?.dateStart).format(DATE_FORMAT.DATE) : ''}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Nhóm">
            <Typography level="text" className="fw-bolder">
              {documentaryGroupMapping(data?.group)}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Loại">
            <Typography level="text" className="fw-bolder">
              {documentaryTypeMapping(data?.type)}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Nội dung">
            <Typography level="text" className="fw-bolder">
              {data?.content}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Người ký">
            <Typography level="text" className="fw-bolder">
              {data?.employeeProfileSigner?.fullName}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Nơi nhận">
            <Typography level="text" className="fw-bolder">
              {data?.recipient}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Nơi lưu">
            <Typography level="text" className="fw-bolder">
              {data?.archiveLocation}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Người gửi / nhận" span={2}>
            <Typography level="text" className="fw-bolder">
              {data?.employeeProfileSender?.fullName}
            </Typography>
          </Descriptions.Item>

        </Descriptions>

        <div className="divider" />

        <Descriptions title="Tệp đính kèm" size="large" layout="vertical" bordered column={5}>
          <Descriptions.Item label="Đính kèm">
            <Row>
              <Flex direction="column" gap={16}>
                <Flex gap={12} flexWrap="wrap">
                  {data?.attachments?.map((item: IBodyFile) => {
                    return <AttachmentPreview key={item?.id} name={item?.fileName} fileUrl={`${FILE_UTIL}/${item?.id}`} />;
                  })}
                </Flex>
              </Flex>
            </Row>
          </Descriptions.Item>
        </Descriptions>
      </Flex>
    </Modal>
  );
};

export default DocumentaryDetailModals;
