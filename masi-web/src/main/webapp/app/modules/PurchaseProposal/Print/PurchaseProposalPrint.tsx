import usePaymentRequest from 'app/hooks/use-payment-request';
import './style.scss';
import useEmployee from 'app/hooks/use-employee';
import { DATE_FORMAT, FILE_UTIL } from 'app/constants/common';
import dayjs from 'dayjs';
import {
  convertCurrency,
  convertToVietnameseCurrency,
} from 'app/shared/util/format';
import { IPaymentRequest } from 'app/shared/model/payment-request.model';
import useSuppliesRequest from 'app/hooks/use-supplies-request';

const { useGetEmployeesQuery, useGetEmployeeProfileByIdQuery } = useEmployee;
const { useGetSuppliesRequestById } = useSuppliesRequest;

interface Props {
  id: string;
}

const PurchaseProposalPrint = ({ id }: Props) => {
  const { data } = useGetSuppliesRequestById(id);
  const { data: employees } = useGetEmployeesQuery();
  const { data: requestByEmployee } = useGetEmployeeProfileByIdQuery(
    data?.requestByEmployeeId,
  );

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

  const totalNonVat = convertCurrency(
    data?.suppliesItemDTO?.reduce(
      (acc, obj) => acc + (Number(obj?.totalAmount ?? 0) ?? 0),
      0,
    ),
  );

  const totaVat = convertCurrency(
    data?.suppliesItemDTO?.reduce(
      (acc, obj) => acc + ((Number(obj?.totalAmountAfterVat ?? 0) ?? 0) - (Number(obj?.totalAmount ?? 0))),
      0,
    ),
  );

  return (
    <section className="purchase-proposal-print-section">
      <div className="purchase-proposal-top">
        <div className="top-item-group">
          <img
            alt="logo"
            className="top-item-logo"
            src="content/images/masilogo.png"
          />
          <div className="top-item-center">
            <span className="top-item-title">PHIẾU ĐỀ XUẤT MUA HÀNG</span>
          </div>
        </div>
      </div>

      <div className="purchase-proposal-content">
        <div className="content-item-group">
          <span className="content-item-bold">
            Người đề xuất: {requestByEmployee?.fullName}
          </span>
        </div>
        <span className="content-item-bold">
          Bộ phận:{' '}
          {getEmployeeProfile(data?.requestByEmployeeId)?.workspace?.name}
        </span>
      </div>
      <div className="purchase-proposal-table">
        <div className="table-container">
          <table>
            <colgroup>
              <col style={{ width: '5%' }} />
              <col style={{ width: '20%' }} />
              <col style={{ width: '5%' }} />
              <col style={{ width: '10%' }} />
              <col style={{ width: '10%' }} />
              <col style={{ width: '10%' }} />
              <col style={{ width: '20%' }} />
              <col style={{ width: '20%' }} />
            </colgroup>
            <thead>
              <tr>
                <th>STT</th>
                <th>Tên hàng hoá</th>
                <th>Đơn vị tính</th>
                <th>Số lượng</th>
                <th>Đơn giá (vnđ)</th>
                <th>Thành tiền (vnđ)</th>
                <th>Nhà cung cấp</th>
                <th>Ghi chú</th>
              </tr>
            </thead>
            <tbody>
              {data?.suppliesItemDTO?.map((x, indx) => (
                <tr key={indx}>
                  <td>{indx + 1}</td>
                  <td>{x?.item?.name}</td>
                  <td>{x?.item?.uom?.name}</td>
                  <td>{x?.quantity}</td>
                  <td>{convertCurrency(x?.item?.unitPrice ?? 0)}</td>
                  <td>{convertCurrency(x?.totalAmount ?? 0)}</td>
                  <td>{data?.supplier?.name}</td>
                  <td rowSpan={data?.suppliesItemDTO?.length + 1}>{x?.note}</td>
                </tr>
              ))}
              <tr>
                <td
                  style={{ textAlign: 'center', fontWeight: '600' }}
                  colSpan={5}
                >
                  TỔNG CỘNG (chưa bao gồm VAT)
                </td>
                <td>{totalNonVat}</td>
                <td />
              </tr>
              <tr>
                <td
                  style={{ textAlign: 'center', fontWeight: '600' }}
                  colSpan={5}
                >
                  VAT {(data?.suppliesItemDTO[0].vat ?? 0)}%
                </td>
                <td>
                  {totaVat}
                </td>
                <td />
              </tr>
              <tr>
                <td
                  style={{ textAlign: 'center', fontWeight: '600' }}
                  colSpan={5}
                >
                  TỔNG TIỀN THANH TOÁN (bao gồm VAT)
                </td>
                <td>{data?.totalAmountAfterVat}</td>
                <td />
              </tr>
            </tbody>
          </table>
        </div>
        <span
          style={{
            fontSize: '14px',
            float: 'right',
            marginTop: '10px',
          }}
        >
          Thành phố Hồ Chí Minh,{' '}
          {dayjs(new Date()).format('ngày DD [tháng] MM [năm] YYYY')}
        </span>
      </div>

      <div className="purchase-proposal-footer">
        <div className="footer-grid">
          {data?.requestApprovals?.map((x, idx) =>
            renderFooterItem(x.employeeId, x.approvedSign, null),
          )}
        </div>
      </div>
    </section>
  );
};

export default PurchaseProposalPrint;
