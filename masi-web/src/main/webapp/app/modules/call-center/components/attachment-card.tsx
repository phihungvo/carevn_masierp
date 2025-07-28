import { Card, Tag } from 'antd';
import Flex from 'app/components/flex/flex';

interface IAttachmentCardProps {
  name: string;
  status: 'processing' | 'success';
  code: string;
  date: string;
}

const AttachmentCard = ({ name, status, code, date }: IAttachmentCardProps) => {
  return (
    <Card className="card-cs" style={{ width: 300 }}>
      <Flex direction="column" justify="space-between" gap={8}>
        <Flex justify="space-between" align="center">
          <span style={{ color: '#344054', fontWeight: 500, fontSize: 14 }}>
            {name}
          </span>
          <span
            style={{
              width: '77px',
              height: '24px',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              borderRadius: '6px',
              fontSize: '12px',
              fontWeight: '500',
              color: status === 'processing' ? '#026AA2' : '#B42318',
              background: status === 'processing' ? '#B2DDFF' : '#FECDCA',
            }}
          >
            {status === 'processing' ? 'Tiếp nhận' : 'Đóng case'}
          </span>
        </Flex>
        <Flex justify="space-between" align="center">
          <span style={{ color: '#475467', fontWeight: 500, fontSize: 14 }}>
            {code}
          </span>
          <span style={{ color: '#475467', fontWeight: 500, fontSize: 14 }}>
            {date}
          </span>
        </Flex>
      </Flex>
    </Card>
  );
};

export default AttachmentCard;
