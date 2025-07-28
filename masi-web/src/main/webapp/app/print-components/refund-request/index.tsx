import { DATE_FORMAT, FILE_UTIL } from 'app/constants/common';
import useEmployee from 'app/hooks/use-employee';
import usePaymentRequest from 'app/hooks/use-payment-request';
import { convertCurrency } from 'app/shared/util/format';
import dayjs from 'dayjs';
import './style.scss';
import { IPaymentRequest } from 'app/shared/model/payment-request.model';

const { useGetPaymentRequestById } = usePaymentRequest;
const { useGetEmployeesQuery } = useEmployee;

interface RefundRequestPrintProps {
  id: string;
}
export const RefundRequestPrint = ({ id }: RefundRequestPrintProps) => {
  const { data } = useGetPaymentRequestById(id);
  const { data: employees } = useGetEmployeesQuery();

  const getEmployeeProfile = (empId: string) => {
    const empSelected = employees?.data?.find(x => x.id === empId);
    return empSelected;
  };

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

  // I. Phiếu tạm ứng
  const calcTotalReimbursement = (data: IPaymentRequest) => {
    return data?.reimbursementDTOS?.reduce(
      (acc, obj) =>
        acc + (Number(obj?.advancement?.paymentVoucherAmount ?? 0) ?? 0),
      0,
    );
  };

  // II. Số tiền đã chi
  const calcTotalPaymentDetails = (data: IPaymentRequest) => {
    return data?.paymentDetails?.reduce(
      (acc, obj) => acc + (Number(obj?.incomingInvoice?.totalAmount ?? 0) ?? 0),
      0,
    );
  };

  const calcRemainingBalance = (data: IPaymentRequest) => {
    const calc =
      (calcTotalReimbursement(data) ?? 0) -
      (calcTotalPaymentDetails(data) ?? 0);
    return calc > 0 ? calc : 0;
  };

  const calcOverSpent = (data: IPaymentRequest) => {
    const calc =
      (calcTotalPaymentDetails(data) ?? 0) -
      (calcTotalReimbursement(data) ?? 0);
    return calc > 0 ? calc : 0;
  };

  return (
    <section className="refund-request-print-section">
      <div className="refund-request-top">
        <div className="top-item-group">
          <img
            alt="logo"
            className="top-item-logo"
            src="content/images/masilogo.png"
          />
          <div className="top-item-center">
            <span className="top-item-title">GIẤY HOÀN TẠM ỨNG</span>
            <span className="top-item-sub-title">Số: {data?.code}</span>
          </div>
          <div className="top-item-contact">
            <span className="top-item-text">QT.8.5.1-01/BM02/TCKT</span>
          </div>
        </div>
      </div>
      <div className="refund-request-header">
        <div className="header-item-right">
          <span className="header-item-text">
            Thành phố Hồ Chí Minh,{' '}
            {dayjs(new Date()).format('ngày DD [tháng] MM [năm] YYYY')}
          </span>
        </div>
      </div>
      <div className="refund-request-content">
        <div className="content-item-group">
          <span>Họ tên nhân viên: {data?.employee?.fullName}</span>
          <span>Mã nhân viên: {data?.employee?.employeeCode}</span>
        </div>
        <span className="content-item-text">
          Bộ phận: {getEmployeeProfile(data?.employeeId)?.workspace?.name}
        </span>
        <span className="content-item-text">
          Nội dung hoàn tạm ứng: {data?.content}
        </span>
      </div>
      <div className="refund-request-table">
        <div className="table-container">
          <span className="table-title">I. Số tiền tạm ứng</span>
          <table>
            <colgroup>
              <col style={{ width: '5%' }} />
              <col style={{ width: '20%' }} />
              <col style={{ width: '15%' }} />
              <col style={{ width: '20%' }} />
              <col style={{ width: '40%' }} />
            </colgroup>
            <thead>
              <tr>
                <th>STT</th>
                <th>Phiêu chi số</th>
                <th>Ngày</th>
                <th>Số tiền</th>
                <th>Ghi chú</th>
              </tr>
            </thead>
            <tbody>
              {data?.reimbursementDTOS?.map((x, indx) => (
                <tr key={indx}>
                  <td>{indx + 1}</td>
                  <td>{x?.advancement?.code}</td>
                  <td>
                    {dayjs(x?.advancement?.createdDate).format(
                      DATE_FORMAT.DATE,
                    )}
                  </td>
                  <td>{convertCurrency(x?.advancement?.totalAmount ?? 0)}</td>
                  <td>{x?.advancement?.note}</td>
                </tr>
              ))}
              <tr>
                <td colSpan={3} />
                <td style={{ textAlign: 'right', fontWeight: '600' }}>
                  {convertCurrency(
                    data?.reimbursementDTOS?.reduce(
                      (acc, obj) =>
                        acc + (Number(obj?.advancement?.totalAmount ?? 0) ?? 0),
                      0,
                    ),
                  )}
                </td>
                <td />
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div className="refund-request-table">
        <div className="table-container">
          <span className="table-title">II. Số tiền đã chi</span>
          <table>
            <colgroup>
              <col style={{ width: '5%' }} />
              <col style={{ width: '20%' }} />
              <col style={{ width: '15%' }} />
              <col style={{ width: '20%' }} />
              <col style={{ width: '40%' }} />
            </colgroup>
            <thead>
              <tr>
                <th>STT</th>
                <th>Chứng từ số</th>
                <th>Ngày</th>
                <th>Số tiền</th>
                <th>Ghi chú</th>
              </tr>
            </thead>
            <tbody>
              {data?.paymentDetails?.map((x, index) => (
                <tr key={index}>
                  <td>{index + 1}</td>
                  <td>{x?.incomingInvoice?.invoiceNo}</td>
                  <td>
                    {dayjs(x?.incomingInvoice?.invoiceDate).format(
                      DATE_FORMAT.DATE,
                    )}
                  </td>
                  <td>{convertCurrency(x?.incomingInvoice?.totalAmount)}</td>
                  <td>{x?.incomingInvoice?.note}</td>
                </tr>
              ))}
              <tr>
                <td colSpan={3}></td>
                <td style={{ textAlign: 'right', fontWeight: '600' }}>
                  {convertCurrency(
                    data?.paymentDetails?.reduce(
                      (acc, obj) =>
                        acc + (Number(obj?.incomingInvoice?.totalAmount) ?? 0),
                      0,
                    ),
                  )}
                </td>
                <td></td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div className="refund-request-table no-border">
        <div className="table-container">
          <span className="table-title">III. Chênh lệch</span>
          <table>
            <colgroup>
              <col style={{ width: '40%' }} />
              <col style={{ width: '20%' }} />
              <col style={{ width: '40%' }} />
            </colgroup>
            <tbody>
              <tr>
                <td>Số tạm ứng chi không hết (I-II)</td>
                <td>{convertCurrency(calcRemainingBalance(data))}</td>
                <td></td>
              </tr>
              <tr>
                <td>Chi quá tạm ứng (II-I)</td>
                <td>{convertCurrency(calcOverSpent(data))}</td>
                <td></td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div className="refund-request-footer">
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
