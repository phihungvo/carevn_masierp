import { Flex } from 'antd';
import { DATE_FORMAT, FILE_UTIL } from 'app/constants/common';
import useEmployee from 'app/hooks/use-employee';
import useProductionQualityControl from 'app/hooks/use-production-quality-control';
import recruitmentMapping from 'app/modules/recruitment/recruitment-mapping';
import dayjs from 'dayjs';
import './style.scss';

const { useGetQualityCheckSampleById } = useProductionQualityControl;
const { useGetEmployeesQuery } = useEmployee;
const { recruitmentPositionTextMapping } = recruitmentMapping;

interface CancelTestingPrintProps {
  id: string;
}

const CancelTestingPrint = ({ id }: CancelTestingPrintProps) => {
  const { data } = useGetQualityCheckSampleById(id);
  const { data: employees } = useGetEmployeesQuery();

  const customDateFormat = (date: dayjs.Dayjs): string => {
    const day = date.date();
    const month = date.month() + 1; // month() is zero-indexed
    const year = date.year();
    return `Ngày ${day} tháng ${month} năm ${year}`;
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

  return (
    <section className="cancel-testing-print-section">
      <div className="cancel-testing-top">
        <div className="top-item-group">
          <img
            alt="logo"
            className="top-item-logo"
            src="content/images/masilogo.png"
          />
          <div className="top-item-contact">
            <span className="top-item-text-sub">Mã số tài liệu: BM.19.03</span>
            <span className="top-item-text-sub">Lần ban hành: 01</span>
            <span className="top-item-text-sub">
              Ngày ban hành: {dayjs().format(DATE_FORMAT.DATE)}
            </span>
            <span className="top-item-text-sub">Trang: 1/1</span>
          </div>
        </div>
      </div>
      <div className="cancel-testing-header">
        <div className="header-item-center">
          <span className="header-item-title">BIÊN BẢN HỦY MẪU</span>
        </div>
      </div>

      <div className="cancel-testing-table">
        <Flex vertical gap={20}>
          <Flex vertical gap={4}>
            <span>{customDateFormat(dayjs())}</span>
            <span>Thành phần tham gia:</span>
            <div className="table-container">
              <table>
                <colgroup>
                  <col style={{ width: '50%' }} />
                  <col style={{ width: '50%' }} />
                </colgroup>
                <thead>
                  <tr>
                    <th>Họ và tên</th>
                    <th>Chức vụ</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td style={{ textAlign: 'left' }}>
                      {
                        employees?.data?.find(
                          item => item?.id === data?.data?.samplingEmployeeId,
                        )?.employeeProfile?.fullName
                      }
                    </td>
                    <td>
                      {recruitmentPositionTextMapping(
                        employees?.data?.find(
                          item => item?.id === data?.data?.samplingEmployeeId,
                        )?.employeeProfile?.position,
                      )}
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </Flex>
          <Flex vertical gap={4}>
            <span>Lý do huỷ mẫu: {data?.data?.disposal?.disposalNote}</span>
            <span>Số lượng: {data?.data?.sampleWeight}</span>
            <div className="table-container">
              <table>
                <thead>
                  <tr>
                    <th>STT</th>
                    {/* <th>Tên Mẫu/ hàng</th> */}
                    <th>Mã mẫu/ hàng</th>
                    <th>Số lượng</th>
                    <th>Ngày lưu</th>
                    <th>Ngày xả</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td style={{ textAlign: 'left' }}>1</td>
                    {/* <td></td> */}
                    <td>{data?.data?.sampleNo}</td>
                    <td>{data?.data?.sampleWeight}</td>
                    <td>
                      {dayjs(data?.data?.disposal?.quantitySaveDate).format(
                        DATE_FORMAT.DATE,
                      )}
                    </td>
                    <td>
                      {dayjs(data?.data?.disposal?.quantityReleaseDate).format(
                        DATE_FORMAT.DATE,
                      )}
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
            <span style={{ minHeight: '60px' }}>
              Phương pháp: {data?.data?.disposal?.disposalMethod}
            </span>
            <span>Kết quả huỷ: {data?.data?.disposal?.disposalResult}</span>
          </Flex>
        </Flex>
      </div>

      <div className="cancel-testing-footer">
        <div className="footer-grid">
          <div className="footer-item">
            <span className="footer-item-container">
              <span className="footer-item-text">Người phê duyệt</span>
              <span style={{ minHeight: '20px' }}>
                {
                  employees?.data?.find(
                    e => e.id === data?.data?.disposal?.reviewerId,
                  )?.employeeProfile?.fullName
                }
              </span>
            </span>
          </div>

          <div className="footer-item">
            <span className="footer-item-container">
              <span className="footer-item-text">Người tạo</span>
              <span style={{ minHeight: '20px' }}>
                {
                  employees?.data?.find(
                    e => e.id === data?.data?.disposal?.involveEmployee,
                  )?.employeeProfile?.fullName
                }
              </span>
            </span>
          </div>
        </div>
      </div>
    </section>
  );
};

export default CancelTestingPrint;
