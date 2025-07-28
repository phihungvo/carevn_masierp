import React from 'react';
import './style.scss';
import dayjs from 'dayjs';
import { DATE_FORMAT, FILE_UTIL } from 'app/constants/common';
import useEmployee from 'app/hooks/use-employee';
import { useLiquidationDetail } from '../../apis/hook';
import { useQuery } from '@tanstack/react-query';
import { DepreciationApi } from 'app/modules/depreciation/apis/axios';
import { convertCurrency } from 'app/shared/util/format';

const { useGetEmployeesQuery } = useEmployee;

interface ILiquidationPrintProps {
  id: string;
}

const LiquidationPrint = ({ id }: ILiquidationPrintProps) => {
  const customDateFormat = (date: dayjs.Dayjs): string => {
    const day = date.date();
    const month = date.month() + 1; // month() is zero-indexed
    const year = date.year();
    return `Ngày ${day} tháng ${month} năm ${year}`;
  };

  const { data } = useLiquidationDetail(id);
  const { data: employees } = useGetEmployeesQuery();
  const { data: properties } = useQuery({
    queryKey: ['properties', 'all'],
    queryFn: DepreciationApi.depriciationList(),
    placeholderData: old => old,
  });

  const renderFooterItem = (empId: string, path: string, updatedAt: string) => {
    const empSelected = employees?.data?.find(x => x.id === empId);
    return (
      <div className="footer-item">
        <span className="footer-item-text">
          {updatedAt
            ? `Ngày ${dayjs(updatedAt)?.format(DATE_FORMAT?.DATE)}`
            : ''}
        </span>
        <span className="footer-item-text">{empSelected?.workspace?.name}</span>
        <span className="footer-item-sign">
          {updatedAt ? <img src={FILE_UTIL + '/' + path} /> : ''}
        </span>
        <span className="footer-item-text">
          {empSelected?.employeeProfile?.fullName}
        </span>
      </div>
    );
  };

  return (
    <section className="liquidation-print-section">
      <div className="liquidation-top">
        <div className="top-item-group">
          <img
            alt="logo"
            className="top-item-logo"
            src="content/images/masilogo.png"
          />
          <div className="top-item-contact">
            <span className="top-item-text-sub top-item-text-bold">
              Mẫu số 02-TSCĐ
            </span>
            <span className="top-item-text-sub">
              (Ban hành theo Thông tư số
            </span>
            <span className="top-item-text-sub">200/2014/TT-BTC</span>
            <span className="top-item-text-sub">
              Ngày 22/12/2014 của Bộ Tài Chính)
            </span>
          </div>
        </div>
      </div>
      <div className="liquidation-header">
        <div className="header-item-center">
          <span className="header-item-title">BIÊN BẢN THANH LÝ TSCĐ</span>
          <span className="header-item-sub-title">
            Ngày .....tháng...... năm ......
          </span>
        </div>
        <span style={{ float: 'right' }}>Số: .............</span>
      </div>

      <div className="liquidation-table">
        <div className="table-container">
          <span className="table-title">I. Ban thanh lý TSCĐ gồm:</span>
          <div>
            <p>
              Ông/Bà: ............................Chức vụ..................Đại
              diện ....................................Trưởng ban
            </p>
            <p>
              Ông/Bà:.............................Chức vụ..................Đại
              diện ....................................Uỷ viên
            </p>
            <p>
              Ông/Bà: ............................Chức vụ..................Đại
              diện ....................................Uỷ viên
            </p>
          </div>
          <span className="table-title">II. Tiến hành thanh lý TSCĐ:</span>
          <table>
            <colgroup>
              <col style={{ width: '15%' }} />
              <col style={{ width: '20%' }} />
              <col style={{ width: '5%' }} />
              <col style={{ width: '20%' }} />
              <col style={{ width: '20%' }} />
              <col style={{ width: '20%' }} />
            </colgroup>
            <thead>
              <tr>
                <th>Mã TS-CC</th>
                <th>Tên TS-CC</th>
                <th>SL</th>
                <th>Nguyên giá</th>
                <th>Khấu hao</th>
                <th>Còn lại</th>
              </tr>
            </thead>
            <tbody>
              {data?.propertyList?.map(p => (
                <tr>
                  <td>
                    {properties?.data?.data?.find(x => x.id === p?.id)?.code}
                  </td>
                  <td>
                    {
                      properties?.data?.data?.find(x => x.id === p?.id)?.item
                        ?.name
                    }
                  </td>
                  <td>{convertCurrency(p?.quantity)}</td>
                  <td>{convertCurrency(p?.originalPrice)}</td>
                  <td>{convertCurrency(p?.depreciation)}</td>
                  <td>{convertCurrency(p?.remainingValue)}</td>
                </tr>
              ))}
            </tbody>
          </table>
          <span className="table-title">
            III. Kết luận của Ban thanh lý TSCĐ:
          </span>
          <p>
            ............................................................................................................................................
          </p>
        </div>
        <div className="liquidation-footer">
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
      </div>
    </section>
  );
};

export default LiquidationPrint;
