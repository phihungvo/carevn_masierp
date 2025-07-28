import React, { useEffect, useState } from 'react';
import { DATE_FORMAT } from "app/constants/common";
import { useGetStocktakingById } from "app/hooks/use-stocktaking";
import dayjs from "dayjs";
import './style.scss';
import { convertCurrency } from 'app/shared/util/format';


const StockTakingPrint = (props: any) => {
  const { id } = props;
  const { data: stockTakingDetail } = useGetStocktakingById(id)
  const [totalActualQuantity, setTotalActualQuantity] = useState(0);
  const [totalSystemQuantity, setTotalSystemQuantity] = useState(0);
  const [listItem, setListItem] = useState([])
  useEffect(() => {
    let listItemTmp = [];
    const dataMap = {}
    const dataMapTotal = {}
    let totalActualQuantityTmp = 0
    let totalSystemQuantityTmp = 0
    for (const stocktaking of (stockTakingDetail?.listInventoriesCheckDetail ?? [])) {
      const key = stocktaking?.item?.itemCategory?.id
      if (!(key in dataMap)) {
        dataMap[key] = []
        dataMapTotal[key] = {
          isGroup: true,
          name: stocktaking?.item?.itemCategory?.name ?? '',
          systemQuantity: 0,
          actualQuantity: 0,
        }
      }
      dataMap[key].push({
        code: stocktaking?.item?.code ?? '',
        name: stocktaking?.item?.name ?? '',
        systemQuantity: stocktaking?.systemQuantity ?? 0,
        actualQuantity: stocktaking?.actualQuantity ?? 0,
      })
      dataMapTotal[key].systemQuantity += stocktaking?.systemQuantity
      dataMapTotal[key].actualQuantity += stocktaking?.actualQuantity
      totalActualQuantityTmp += stocktaking?.actualQuantity
      totalSystemQuantityTmp += stocktaking?.systemQuantity
    }

    for (const key in dataMap) {
      listItemTmp = [...listItemTmp, ...dataMap[key]]
      listItemTmp.push({ ...dataMapTotal[key] })
    }
    let index = 0;
    listItemTmp = listItemTmp.map(item => {
      if (!item.isGroup) {
        index += 1
      }
      return { ...item, index: index }
    })

    setListItem([...listItemTmp])
    setTotalActualQuantity(totalActualQuantityTmp)
    setTotalSystemQuantity(totalSystemQuantityTmp)
  }, [stockTakingDetail])

  return (<section className="stock-taking-print-section">
    <div>CARE VIETNAM</div>
    <div className="title">BIÊN BẢN KIỂM KÊ HÀNG HÓA</div>
    <div className="subTitle">Ngày {dayjs().format(DATE_FORMAT.DATE)}</div>
    <div>
      <div>Kho {`${stockTakingDetail?.warehouse?.code} - ${stockTakingDetail?.warehouse?.name}`}</div>
    </div>
    <div>
      <table className="table table-striped table-bordered">
        <thead>
          <tr>
            <th rowSpan={2}>STT</th>
            <th rowSpan={2}>Mã hàng hoá</th>
            <th rowSpan={2}>Tên hàng hoá</th>
            <th colSpan={2}>Tồn sổ sách</th>
            <th colSpan={2}>Kiểm kê thực tế</th>
            <th colSpan={2}>Chênh lệch</th>
          </tr>
          <tr>
            <th>Số lượng</th>
            <th>Kgs</th>
            <th>Số lượng</th>
            <th>Kgs</td>
            <th>Số lượng</th>
            <th>Kgs</th>
          </tr>
        </thead>
        {
          listItem.map(item => {
            if (item?.isGroup) {
              return (<>
                <tr>
                  <td className="centerData bold" colSpan={3}>{item?.name ?? ''}</td>
                  <td className="rightData bold">{convertCurrency(item?.systemQuantity)}</td>
                  <td></td>
                  <td className="rightData bold">{convertCurrency(item?.actualQuantity)}</td>
                  <td></td>
                  <td className="rightData bold">{convertCurrency(item.systemQuantity - item.actualQuantity)}</td>
                  <td></td>
                </tr>
              </>)
            }
            return (<>
              <tr>
                <td className="centerData">{item.index}</td>
                <td>{item.code}</td>
                <td>{item.name}</td>
                <td className="rightData">{convertCurrency(item.systemQuantity)}</td>
                <td></td>
                <td className="rightData">{convertCurrency(item.actualQuantity)}</td>
                <td></td>
                <td className="rightData">{convertCurrency(item.systemQuantity - item.actualQuantity)}</td>
                <td></td>
              </tr>
            </>)
          })
        }
        <tr>
          <td className="centerData bold" colSpan={3}>Cộng Kho: {`${stockTakingDetail?.warehouse?.code} - ${stockTakingDetail?.warehouse?.name}`}</td>
          <td className="rightData bold">{convertCurrency(totalSystemQuantity)}</td>
          <td></td>
          <td className="rightData bold">{convertCurrency(totalActualQuantity)}</td>
          <td></td>
          <td className="rightData bold">{convertCurrency(totalSystemQuantity - totalActualQuantity)}</td>
          <td></td>
        </tr>
        <tr>
          <td className="rightData pr-1 bold" colSpan={3}>Tổng cộng</td>
          <td className="rightData bold">{convertCurrency(totalSystemQuantity)}</td>
          <td></td>
          <td className="rightData bold">{convertCurrency(totalActualQuantity)}</td>
          <td></td>
          <td className="rightData bold">{convertCurrency(totalSystemQuantity - totalActualQuantity)}</td>
          <td></td>
        </tr>
      </table>
    </div>
    <div className="footer">
      <div className="bold">Trưởng đơn vị</div>
      <div className="bold">Kết toán</div>
      <div>
        <div>{dayjs().format(DATE_FORMAT.DATE_STRING)}</div>
        <div className="text-center bold">Người lập</div>
      </div>
    </div>
  </section>)
}

export default StockTakingPrint;

