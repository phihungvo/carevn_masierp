import React, { useState } from 'react';
import './style.scss';
import { Flex } from 'antd';
import { Typography } from 'app/components/typography/typography';
import dayjs from 'dayjs';
import { Row, Col } from 'reactstrap';
import { useGetSupplierContractsById } from 'app/hooks/use-supplier-contract';
import useItems from 'app/hooks/use-items';
import { convertCurrency, convertToVietnameseCurrency } from 'app/shared/util/format';
import { number } from 'zod';

const { useGetItemsQuery } = useItems;

interface ISupplierContractPrintProps {
  id: string;
}

const SupplierContractPrint = ({ id }: ISupplierContractPrintProps) => {
  const customDateFormat = (date: dayjs.Dayjs): string => {
    const day = date.date();
    const month = date.month() + 1; // month() is zero-indexed
    const year = date.year();
    return `ngày ${day} tháng ${month} năm ${year}`;
  };

  const { data } = useGetSupplierContractsById(id);

  const { data: items } = useGetItemsQuery();

  let total = 0

  return (
    <section className="supplier-contract-print-section">
      <Flex vertical justify="center" align="center" gap={20}>
        <Flex vertical justify="center" align="center">
          <p className="font-bold">CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM</p>
          <u>Độc lập - Tự do - Hạnh phúc</u>
        </Flex>

        <Flex vertical justify="center" align="center">
          <Typography level={4}>HỢP ĐỒNG MUA HÀNG</Typography>
          <p>Số: ___/___/HĐMB-KD</p>
        </Flex>
      </Flex>
      <p>Hôm nay, {customDateFormat(dayjs())}, đại diện hai bên gồm:</p>
      <Flex
        vertical
        style={{
          paddingInline: 40,
        }}
      >
        <p className="font-bold">
          <u>BÊN A</u> (BÊN MUA): CÔNG TY
        </p>
        <Flex>
          <span className="title-inline">Địa chỉ</span>
          <span>:</span>
        </Flex>
        <Row>
          <Col xs={6}>
            <span className="title-inline">Điện Thoại</span>
            <span>:</span>
          </Col>
          <Col xs={6}>
            <span>Fax</span>
            <span>:</span>
          </Col>
        </Row>
        <Flex>
          <span className="title-inline">Tài Khoản</span>
          <span>:</span>
        </Flex>
        <Flex>
          <span className="title-inline">Chủ Tài Khoản</span>
          <span>:</span>
        </Flex>
        <Flex>
          <span className="title-inline">Tên Ngân Hàng</span>
          <span>:</span>
        </Flex>
        <Flex>
          <span className="title-inline">Mã số thuế </span>
          <span>:</span>
        </Flex>
        <Row>
          <Col xs={6}>
            <span className="title-inline">Đại Diện</span>
            <span>:</span>
          </Col>
          <Col xs={6}>
            <span>Chức vụ</span>
            <span>:</span>
          </Col>
        </Row>
        <p
          className="font-bold"
          style={{
            marginBottom: 20,
          }}
        >
          <u>BÊN B</u> (BÊN BÁN): CÔNG TY
        </p>
        <Flex>
          <span className="title-inline">Địa chỉ</span>
          <span>: {data?.supplier?.address}</span>
        </Flex>
        <Row>
          <Col xs={6}>
            <span className="title-inline">Điện Thoại</span>
            <span>: {data?.supplier?.phone}</span>
          </Col>
          <Col xs={6}>
            <span>Fax</span>
            <span>: {data?.supplier?.fax}</span>
          </Col>
        </Row>
        <Flex>
          <span className="title-inline">Tài Khoản</span>
          <span>:</span>
        </Flex>
        <Flex>
          <span className="title-inline">Chủ Tài Khoản</span>
          <span>:</span>
        </Flex>
        <Flex>
          <span className="title-inline">Tên Ngân Hàng</span>
          <span>: {data?.supplier?.bankInfo}</span>
        </Flex>
        <Flex>
          <span className="title-inline">Mã số thuế </span>
          <span>: {data?.supplier?.taxCode}</span>
        </Flex>
        <Row>
          <Col xs={6}>
            <span className="title-inline">Đại Diện</span>
            <span>: {data?.supplier?.fullName}</span>
          </Col>
          <Col xs={6}>
            <span>Chức vụ</span>
            <span>: {data?.supplier?.position}</span>
          </Col>
        </Row>
        <p>
          Sau khi thỏa thuận, hai bên cùng thống nhất ký kết Hợp Đồng Mua Bán
          (“Hợp đồng”) với các điều khoản và điều kiện sau:
        </p>
      </Flex>
      <p className="font-bold">
        <u>ĐIỀU 1: HÀNG HÓA – GIÁ BÁN</u>
      </p>
      <table>
        <colgroup>
          <col style={{ width: '10%' }} />
          <col style={{ width: '20%' }} />
          <col style={{ width: '20%' }} />
          <col style={{ width: '15%' }} />
          <col style={{ width: '15%' }} />
          <col style={{ width: '20%' }} />
        </colgroup>
        <thead>
          <tr>
            <th>Stt</th>
            <th>Mặt hàng</th>
            <th>Xuất xứ</th>
            <th>Số lượng (kg)</th>
            <th>Đơn giá (vnđ/...)</th>
            <th>Thành tiền (vnđ)</th>
          </tr>
        </thead>
        <tbody>
          {data?.supplierContractDetails?.map((detail, idx) => {
            const findItem = items?.data?.find(
              item => item?.id === detail?.supplyItemId,
            );
            total = (total + Number(detail?.price) * Number(detail?.quantity))
            return (
              <tr>
                <td>{idx + 1}</td>
                <td>{`${findItem?.code} - ${findItem?.name}`}</td>
                <td>{findItem?.attribute?.general?.countryMade}</td>
                <td>{detail?.quantity}</td>
                <td>{convertCurrency(detail?.price)}</td>
                <td>
                  ({convertCurrency(
                    Number(detail?.price) * Number(detail?.quantity))
                  })
                </td>
              </tr>
            );
          })}

          <tr>
            <td
              colSpan={5}
              className="font-bold"
              style={{
                textAlign: 'center',
              }}
            >
              TỔNG
            </td>
            <td>
              { convertCurrency(total) }
            </td>
          </tr>
          <tr>
            <td
              colSpan={5}
              className="font-bold"
              style={{
                textAlign: 'center',
              }}
            >
              THUẾ VAT {data?.supplierContractDetails[0].vatRate ?? 0}%
            </td>
            <td>{ convertCurrency((Number(data?.supplierContractDetails[0]?.vatRate ?? 0) *  total) / 100) }</td>
          </tr>
          <tr>
            <td colSpan={5}>
              <Flex vertical align="center" justify="center">
                <span className="font-bold">TỔNG GIÁ TRỊ</span>
                <i>
                  (Đã bao gồm Thuế VAT {data?.supplierContractDetails[0].vatRate ?? 0}%. Đơn giá không bao gồm phí vận
                  chuyển đến kho của Bên A)
                </i>
              </Flex>
            </td>
            <td>{convertCurrency(((Number(data?.supplierContractDetails[0]?.vatRate ?? 0) *  total) / 100)  + total) }</td>
          </tr>
        </tbody>
      </table>
      <p
        className="font-bold"
        style={{
          fontSize: 14,
        }}
      >
        <i>Bằng chữ: { convertToVietnameseCurrency(((Number(data?.supplierContractDetails[0]?.vatRate ?? 0) *  total) / 100)  + Number(total))} đồng.</i>
      </p>
      <p className="font-bold">
        <u>ĐIỀU 2: CHẤT LƯỢNG, QUY CÁCH HÀNG HÓA</u>
      </p>
      <Row>
        <Col xs={1}>2.1.</Col>
        <Col xs={11}>
          Tem nhãn hàng hoá: Phải có ghi đầy đủ, rõ ràng các thông tin bắt buộc
          theo quy định của pháp luật về ghi nhãn hàng hóa: Tên nhà sản xuất,
          cách bảo quản, hạn sử dụng. Đối với các sản phẩm nhập khẩu: Có thêm
          nhãn hàng hóa bằng tiếng Việt theo đúng quy định liên quan.
        </Col>
        <Col xs={1}>2.2.</Col>
        <Col xs={11}>
          Chất lượng hàng hóa: Bảo đảm sử dụng an toàn theo tiêu chuẩn của nhà
          sản xuất, không bị biến chất, thay đổi so với qui cách ban đầu do lỗi
          bảo quản hoặc vận chuyển của Bên B, hàng còn tối thiểu từ ½ thời gian
          sử dụng in trên bao bì sản phẩm.
        </Col>
        <Col xs={1}>2.3.</Col>
        <Col xs={11}>
          Bên B cam kết hoàn toàn chịu trách nhiệm về nguồn gốc xuất xứ, khối
          lượng, chất lượng sản phẩm, phải cung cấp đầy đủ các giấp chứng nhận,
          tiêu chuẩn chất lượng hàng hóa phù hợp với các qui định pháp luật hiện
          hành.
        </Col>
      </Row>
      <p className="font-bold">
        <u>ĐIỀU 3: PHƯƠNG THỨC THANH TOÁN</u>
      </p>
      <Row>
        <Col xs={1}>3.1.</Col>
        <Col xs={11}>
          Phương thức thanh toán: Bên A chuyển khoản trực tiếp vào tài khoản
          ngân hàng của Bên B, như thông tin của Hợp Đồng.
        </Col>
        <Col xs={1}>3.2.</Col>
        <Col xs={11}>
          <p>Thời hạn thanh toán:</p>
          <p>Bên A sẽ thanh toán cho Bên B thành hai (02) đợt như sau:</p>
          <Row>
            <Col xs={1}>a.</Col>
            <Col xs={11}>
              <b>Lần 1: </b>
              <span>
                Tạm ứng <i className="title-secondary">[…]%</i> trị giá Hợp Đồng
                tương ứng số tiền{' '}
                <i className="title-secondary">[điền số tiền]</i> đồng (Bằng
                chữ: <i className="title-secondary">[điền số tiền bằng chữ]</i>)
                trong vòng bảy (07) ngày làm việc kể từ ngày ký Hợp Đồng.
              </span>
            </Col>
            <Col xs={1}>a.</Col>
            <Col xs={11}>
              <b>Lần 2: </b>
              <span>
                Thanh toán <i className="title-secondary">[…]%</i> giá trị còn
                lại của Hợp Đồng tương ứng số tiền{' '}
                <i className="title-secondary">[điền số tiền]</i> đồng (Bằng
                chữ: <i className="title-secondary">[điền số tiền bằng chữ]</i>)
                trong vòng bảy (07) ngày làm việc kể từ khi Bên A nhận được đầy
                đủ hàng hóa, biên bản bàn giao hàng hóa, hóa đơn GTGT hợp lệ.
              </span>
            </Col>
          </Row>
        </Col>
        <Col xs={1}>
          <b>
            <u>3.2.</u>
          </b>
        </Col>
        <Col xs={11}>
          Trong trường hợp đến thời hạn thanh toán theo quy định tại Hợp Đồng
          này mà Bên A vẫn không thực hiện thanh toán cho Bên B thì Bên A có
          nghĩa vụ thanh toán khoản tiền chậm thanh toán theo lãi suất 0.02%/mỗi
          ngày chậm trễ của khoản thanh toán chậm được tính từ ngày đến hạn
          thanh toán đến ngày thực tế thanh toán.
        </Col>
      </Row>
      <p className="font-bold">
        <u>ĐIỀU 4: GIAO NHẬN HÀNG HÓA - NGHIỆM THU:</u>
      </p>
      <Row>
        <Col xs={1} className="font-bold">
          4.1.
        </Col>
        <Col xs={11}>
          <b>Phương thức giao nhận:</b>
          <Row>
            <Col xs={1}>a</Col>
            <Col xs={11}>
              Việc giao nhận hàng hóa được đại diện có thẩm quyền của các bên
              xác nhận bằng văn bản.
            </Col>
            <Col xs={1}>b</Col>
            <Col xs={11}>
              Việc Bên A ký xác nhận vào Biên bản giao nhận/ nghiệm thu hàng hóa
              không làm miễn trừ trách nhiệm đổi trả hàng hóa của Bên B.
            </Col>
          </Row>
        </Col>
        <Col xs={1}>4.2.</Col>
        <Col xs={11}>
          <b>Địa điểm giao hàng:</b>
          <span>
            Tại kho Bên A, địa chỉ:{' '}
            <i className="title-secondary">[điền địa chỉ]</i>
          </span>
        </Col>
        <Col xs={1} className="font-bold">
          4.3.
        </Col>
        <Col xs={11}>
          <b>Thời gian giao hàng:</b>
          <Row>
            <Col xs={1}>a</Col>
            <Col xs={11}>
              Thời gian dự kiến: Ngày <i className="title-secondary">[…]</i>{' '}
              tháng <i className="title-secondary">[…]</i> năm{' '}
              <i className="title-secondary">[…]</i>
            </Col>
            <Col xs={1}>b</Col>
            <Col xs={11}>
              Việc Bên A ký xác nhận vào Biên bản giao nhận/ nghiệm thu hàng hóa
              không làm miễn trừ trách nhiệm đổi trả hàng hóa của Bên B.
            </Col>
          </Row>
        </Col>
        <Col xs={1} className="font-bold">
          4.4.
        </Col>
        <Col xs={11}>
          <b>Nghiệm thu:</b>
          <Row>
            <Col xs={1}>-</Col>
            <Col xs={11}>
              Bên B giao hàng theo mẫu, nhãn hiệu hoặc quy cách được quy định
              tại Điều 1, Điều 2 của Hợp đồng này. Tại thời điểm nhận hàng, Bên
              A có trách nhiệm phải kiểm tra hàng về số lượng, chất lượng và đại
              diện hai bên cùng ký vào biên bản giao nhận/nghiệm thu hàng hóa.
              Trường hợp, Bên B giao hàng không đúng với chất lượng mẫu đã thỏa
              thuận, thì Bên A lập biên bản tại chỗ và yêu cầu Bên B xác nhận,
              đồng thời, Bên A có quyền từ chối nhận hàng.
            </Col>
            <Col xs={1}>-</Col>
            <Col xs={11}>
              Sau khi Biên bản giao nhận/nghiệm thu hàng hóa được ký, trong
              trường hợp Bên A phát hiện hàng hóa không đảm bảo chất lượng như
              đã cam kết thì Bên B có nghĩa vụ sửa chữa, khắc phục hoặc đổi hàng
              mới cho Bên A (trừ trường hợp hàng hóa bị hư hỏng, giảm chất lượng
              do lỗi bảo quản, sử dụng của Bên A) trong thời gian 03 ngày kể từ
              ngày Bên A thông báo.
            </Col>
          </Row>
        </Col>
      </Row>
      <p className="font-bold">
        <u>ĐIỀU 5: TRÁCH NHIỆM CỦA BÊN A</u>
      </p>
      <Row>
        <Col xs={1}>5.1.</Col>
        <Col xs={11}>
          Bảo quản và bán hàng hóa đúng hướng dẫn và tính chất sản phẩm.
        </Col>
        <Col xs={1}>5.2.</Col>
        <Col xs={11}>
          Thanh toán cho Bên B đúng theo thời hạn qui định trong Hợp đồng này.
        </Col>
        <Col xs={1}>5.3.</Col>
        <Col xs={11}>
          Nhận hàng, kiểm tra và xác nhận tình trạng hàng hóa tại thời điểm nhận
          hàng.
        </Col>
        <Col xs={1}>5.4.</Col>
        <Col xs={11}>
          Phối hợp với Bên B để giải quyết các khiếu nại của khách hàng liên
          quan đến hàng hóa do Bên B cung cấp cho Bên A.
        </Col>
      </Row>
      <p className="font-bold">
        <u>ĐIỀU 6: TRÁCH NHIỆM CỦA BÊN B</u>
      </p>
      <Row>
        <Col xs={1}>6.1.</Col>
        <Col xs={11}>
          Đảm bảo sản phẩm giao đến kho Bên A đúng và đủ các mặt hàng đã thỏa
          thuận theo Hợp đồng.
        </Col>
        <Col xs={1}>6.2.</Col>
        <Col xs={11}>
          Chịu trách nhiệm về quyền sỡ hữu hợp pháp, nguồn gốc xuất xứ, chất
          lượng hàng hóa. Trong trường hợp hàng hóa không thuộc quyền sỡ hữu hợp
          pháp, đúng xuất xứ, chất lượng theo tiêu chuẩn tại Điều 2 của Hợp
          đồng.
        </Col>
        <Col xs={1}>6.3.</Col>
        <Col xs={11}>
          Có nghĩa vụ sửa chữa, khắc phục hoặc đổi hàng mới cho Bên A (trừ
          trường hợp hàng hóa bị hư hỏng, giảm chất lượng do lỗi bảo quản, sử
          dụng của Bên A) nếu hàng hóa không đảm bảo chất lượng như đã cam kết.
        </Col>
        <Col xs={1}>6.4.</Col>
        <Col xs={11}>Xuất hóa đơn GTGT hợp lệ cho Bên A.</Col>
      </Row>
      <p className="font-bold">
        <u>ĐIỀU 7: NGHĨA VỤ THUẾ VÀ CÁC KHOẢN PHẢI NỘP VỚI CƠ QUAN NHÀ NƯỚC:</u>
      </p>
      <Row>
        <Col xs={1}></Col>
        <Col xs={11}>
          Mỗi bên chịu trách nhiệm độc lập trong việc khai báo, nộp các loại
          thuế và các loại phí khác liên quan hoặc phát sinh từ hợp đồng này với
          cơ quan Thuế theo qui định pháp luật.
        </Col>
      </Row>
      <p className="font-bold">
        <u>ĐIỀU 8: HIỆU LỰC CỦA HỢP ĐỒNG, PHẠT VI PHẠM</u>
      </p>
      <Row>
        <Col xs={1} className="font-bold">
          8.1.
        </Col>
        <Col xs={11}>
          Hợp đồng có hiệu lực kể từ ngày ký đến hết ngày <i>[…………….]</i>, trừ
          trường hợp được chấm dứt sớm theo các quy định tại Hợp đồng này.
        </Col>
        <Col xs={1}>8.2.</Col>
        <Col xs={11}>
          Một trong các bên có thể chấm dứt hợp đồng này bằng văn bản thông báo
          trước 30 (ba mươi) ngày tính từ thời điểm bên còn lại nhận được thông
          báo. Ngày nhận thông báo sẽ được xem là 03 ngày làm việc sau ngày gửi
          thư qua bưu điện, và hai mươi bốn (24) giờ sau khi gửi fax, ngày ký
          vào biên bản bàn giao trong trường hợp nhận trực tiếp, trừ trường hợp
          có bằng chứng xác thực khác để xác định thời gian nhận khác.
        </Col>
        <Col xs={1}>8.3.</Col>
        <Col xs={11}>
          Trong trường hợp một trong Các Bên vi phạm các điều khoản của Hợp Đồng
          này mà không khắc phục vi phạm trong 15 (mười lăm) ngày kể từ ngày một
          bên thông báo về việc vi phạm, thì bên bị vi phạm có quyền chấm dứt
          trước thời hạn hợp đồng với hiệu lực tức thời sau khi kết thúc thời
          hạn này. Đồng thời Bên vi phạm phải:
          <Row>
            <Col xs={1}>a.</Col>
            <Col xs={11}>
              Trả cho Bên bị vi phạm một khoản tiền phạt tương đương 8% (tám
              phần trăm) giá trị phần nghĩa vụ hợp đồng bị vi phạm; và
            </Col>
            <Col xs={1}>b.</Col>
            <Col xs={11}>
              Bồi thường cho Bên bị vi phạm toàn bộ thiệt hại mà Bên bị vi phạm
              phải gánh chịu do hành vi vi phạm của Bên vi phạm gây ra{' '}
              <i>(nếu có)</i>.
            </Col>
          </Row>
        </Col>
      </Row>
      <p className="font-bold">
        <u>ĐIỀU 9: ĐIỀU KHOẢN CHUNG</u>
      </p>
      <Row>
        <Col xs={1}>9.1.</Col>
        <Col xs={11}>
          Hai bên cam kết thực hiện nghiêm túc các điều khoản nói trên.
        </Col>
        <Col xs={1}>9.2.</Col>
        <Col xs={11}>
          Bất kỳ các sửa đổi, bổ sung liên quan đến Hợp đồng này chỉ có hiệu lực
          nếu được lập thành văn bản và được đại diện có thẩm quyền của các bên
          ký kết.
        </Col>
        <Col xs={1}>9.3.</Col>
        <Col xs={11}>
          Trong quá trình thực hiện Hợp đồng nếu có vướng mắc hoặc tranh chấp
          hai bên sẽ cùng giải quyết trên tinh thần hợp tác. Nếu có bất kỳ tranh
          chấp nào mà các bên không thể tự giải quyết bằng thương lượng, hòa
          giải trong vòng 30 (ba mươi) ngày kể từ ngày một bên thông báo tranh
          chấp phát sinh thì bất kỳ bên nào cũng có quyền yêu cầu Tòa án nhân
          dân có thẩm quyền giải quyết theo quy định pháp luật Việt Nam, án phí
          do bên thua chịu.
        </Col>
        <Col xs={1}>9.4.</Col>
        <Col xs={11}>
          Hợp đồng được lập thành <b>04</b> (bốn) bản bằng tiếng Việt, mỗi bên
          giữ <b>02</b> (hai) bản có giá trị pháp lý như nhau.
        </Col>
      </Row>
      <Flex
        style={{
          paddingInline: 40,
        }}
        align="center"
        justify="space-around"
      >
        <Flex vertical align="center" justify="center" gap={100}>
          <b>ĐẠI DIỆN BÊN A</b>
          <b>Ông/Bà […]</b>
        </Flex>
        <Flex vertical align="center" justify="center" gap={100}>
          <b>ĐẠI DIỆN BÊN B</b>
          <b>Ông/Bà […]</b>
        </Flex>
      </Flex>
    </section>
  );
};

export default SupplierContractPrint;
