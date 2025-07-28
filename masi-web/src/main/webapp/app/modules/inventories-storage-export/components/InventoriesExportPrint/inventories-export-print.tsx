import { DATE_FORMAT, FILE_UTIL } from 'app/constants/common';
import useEmployee from 'app/hooks/use-employee';
import usePaymentRequest from 'app/hooks/use-payment-request';
import {
  convertCurrency,
  convertToVietnameseCurrency,
} from 'app/shared/util/format';
import dayjs from 'dayjs';
import './style.scss';
import { useGetInventoriesById } from 'app/hooks/use-inventories';

const { useGetEmployeesQuery } = useEmployee;

interface InventoriesExportPrintProps {
  id: string;
}

export const InventoriesExportPrint = ({ id }: InventoriesExportPrintProps) => {
  const { data } = useGetInventoriesById(id);
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
    <section className="inventories-export-print-section">
      <div className="inventories-export-top">
        <div className="top-item-group">
          <img
            alt="logo"
            className="top-item-logo"
            src="content/images/masilogo.png"
          />
          <div className="top-item-contact">
            <span className="top-item-text-sub">
              <span className="top-item-text-blue">W:</span> masi.vn
            </span>
            <span className="top-item-text-sub">
              <span className="top-item-text-blue">E:</span> cs@masi.vn
            </span>
            <span className="top-item-text-blue">
              Call Center:{' '}
              <span className="top-item-text-sub-blue">0909 411 885</span>
            </span>
          </div>
        </div>
      </div>
      <div className="inventories-export-header">
        <div className="header-item-right">
          <span className="header-item-text">
            Thành phố Hồ Chí Minh,{' '}
            {dayjs(new Date()).format('ngày DD [tháng] MM [năm] YYYY')}
          </span>
        </div>
        <div className="header-item-center">
          <span className="header-item-title">PHIẾU XUẤT KHO</span>
        </div>
      </div>
      <div className="inventories-export-content">
        <span className="content-item-text">
          Họ và tên người nhận: {data?.attribute?.receiverName}
        </span>
        <span className="content-item-text">
          Địa điểm giao hàng: <b>{data?.attribute?.shipperAddress}</b>
        </span>
        <span className="content-item-text">
          Tên tài xế xe: {data?.attribute?.shipper}
        </span>
        <div className="content-item-group">
          <span>Số xe:</span>
          <span>Số rờ móc:</span>
        </div>
        <span className="content-item-text">
          Số điện thoại: {data?.attribute?.receiverPhone}
        </span>
      </div>
      <div className="inventories-export-table">
        <div className="table-container">
          <table>
            <colgroup>
              <col style={{ width: '10%' }} />
              <col style={{ width: '30%' }} />
              <col style={{ width: '20%' }} />
              <col style={{ width: '20%' }} />
              <col style={{ width: '20%' }} />
            </colgroup>
            <thead>
              <tr>
                <th>STT</th>
                <th>TÊN SẢN PHẨM, HÀNG HOÁ</th>
                <th>ĐƠN VỊ TÍNH</th>
                <th>SỐ BAO</th>
                <th>SỐ KG/BAO</th>
              </tr>
            </thead>
            <tbody>
              {data?.inventoriesDetails?.map((x, idx) => (
                <tr key={idx}>
                  <td>{idx + 1}</td>
                  <td>{x?.item?.name}</td>
                  <td>{x?.item?.uom?.name}</td>
                  <td>{x?.quantity}</td>
                  <td>50</td>
                </tr>
              ))}
              <tr>
                <td></td>
                <td>Tổng cộng</td>
                <td colSpan={3}>
                  {convertCurrency(
                    data?.inventoriesDetails?.reduce(
                      (acc, cur) => acc + cur.quantity * 50,
                      0,
                    ),
                  )}{' '}
                  KG
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div className="inventories-export-footer">
        <div className="footer-grid">
          {data?.requestApprovals?.map((x, idx) =>
            renderFooterItem(x.employeeId, x.approvedSign, null),
          )}
        </div>
      </div>

      <div className="inventories-export-footer information">
        <div className="footer-information-grid">
          <div className="footer-information-item">
            <span className="information-office">Head office</span>
            <span className="information-address">
              7th floor, GigaMall Trade Center, 240-242 Pham Van Dong St., Hiep
              Binh Chanh W., Thu Duc City, HCMC
            </span>
          </div>

          <div className="footer-information-item">
            <span className="information-office">Factory</span>
            <span className="information-address">
              Lot C11, Fish Sauce Processing Zone, Phu Hai Ward, Phan Thiet
              City, Binh Thuan Province
            </span>
          </div>
        </div>
      </div>
    </section>
  );
};
