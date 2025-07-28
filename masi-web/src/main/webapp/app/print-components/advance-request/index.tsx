import { DATE_FORMAT, FILE_UTIL } from 'app/constants/common';
import useEmployee from 'app/hooks/use-employee';
import usePaymentRequest from 'app/hooks/use-payment-request';
import {
  convertCurrency,
  convertToVietnameseCurrency,
} from 'app/shared/util/format';
import dayjs from 'dayjs';
import './style.scss';

const { useGetPaymentRequestById } = usePaymentRequest;
const { useGetEmployeesQuery } = useEmployee;

interface AdvanceRequestPrintProps {
  id: string;
}
export const AdvanceRequestPrint = ({ id }: AdvanceRequestPrintProps) => {
  const { data } = useGetPaymentRequestById(id);
  const { data: employees } = useGetEmployeesQuery();

  const renderFooterItem = (empId: string, path: string, updatedAt: string) => {
    const empSelected = employees?.data?.find(x => x.id === empId);
    return (
      <div className="footer-item">
        <span className="footer-item-text">{empSelected?.workspace?.name}</span>
        <span className="footer-item-sign">
          {updatedAt ? <img src={FILE_UTIL + '/' + path} /> : ''}
        </span>
        <span className="footer-item-text">
          {empSelected?.employeeProfile?.fullName}
        </span>
        <span className="footer-item-text">
          {updatedAt
            ? `Ngày ${dayjs(updatedAt)?.format(DATE_FORMAT?.DATE)}`
            : ''}
        </span>
      </div>
    );
  };

  return (
    <section className="advance-request-print-section">
      <div className="advance-request-top">
        <div className="top-item-group">
          <img
            alt="logo"
            className="top-item-logo"
            src="content/images/masilogo.png"
          />
          <div className="top-item-contact">
            <span className="top-item-text-blue">
              Email: cs@caresolutions.com.vn
            </span>
            <span className="top-item-text-blue">Website: carevietnam.vn</span>
            <span className="top-item-text-blue">
              Call Center: 0909 411 885
            </span>
            <span className="top-item-text">QT.8.5.1-01/BM02/TCKT</span>
          </div>
        </div>
      </div>
      <div className="advance-request-header">
        <div className="header-item-center">
          <span className="header-item-title">ĐỀ NGHỊ TẠM ỨNG</span>
          <span className="header-item-sub-title">Số: {data?.code}</span>
        </div>
        <div className="header-item-right">
          <span className="header-item-text">
            Thành phố Hồ Chí Minh,{' '}
            {dayjs(new Date()).format('ngày DD [tháng] MM [năm] YYYY')}
          </span>
        </div>
      </div>
      <div className="advance-request-content">
        <span className="content-item-text">
          Kính gởi: {data?.suppliers.name}
        </span>
        <div className="content-item-group">
          <span>Họ tên nhân viên: {data?.employee?.fullName}</span>
          <span>Mã nhân viên: {data?.employee?.employeeCode}</span>
        </div>
        <span className="content-item-text">
          Số tiền đề nghị tạm ứng: {convertCurrency(data?.totalAmount)}
        </span>
        <span className="content-item-text">
          Bằng chữ: {convertToVietnameseCurrency(data?.totalAmount)}
        </span>
        <span className="content-item-text">
          Nội dung thanh toán: {data?.content}
        </span>
        <span className="content-item-text">
          Thời hạn hoàn tạm ứng:{' '}
          {dayjs(data?.paymentDate).format(DATE_FORMAT.DATE)}
        </span>
      </div>
      <div className="advance-request-footer">
        <div className="footer-grid">
          {data?.requestApprovals?.map((x, idx) =>
            renderFooterItem(
              x.employeeId,
              x.approvedSign,
              x.result && x.updatedAt ? x.updatedAt : null,
            ),
          )}
        </div>
      </div>
    </section>
  );
};
