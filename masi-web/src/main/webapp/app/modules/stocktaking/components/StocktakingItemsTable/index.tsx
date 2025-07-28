import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import Flex from 'app/components/flex/flex';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import TablePagination from 'app/components/table-v2/TablePagination';
import Tooltip from 'app/components/tooltip/tooltip';
import { Typography } from 'app/components/typography/typography';
import {
  DEFAULT_PAGE,
  DEFAULT_PAGE_SIZE,
  DEFAULT_PAGE_SIZE_NAX,
} from 'app/constants/common';
import { useGetInventoriesStorageItemsAllByWarehouse } from 'app/hooks/use-inventories';
import { PaginationParams } from 'app/shared/model/pagination.model';
import { STOCKTAKING_STATUS } from 'app/shared/model/stocktaking.model';
import { convertCurrency } from 'app/shared/util/format';
import { StocktakingSchema } from 'app/validation/stocktaking.validation';
import { useEffect, useState } from 'react';
import { useFormContext } from 'react-hook-form';

export const StocktakingItemsTable = () => {
  const { watch, control, setValue } = useFormContext<StocktakingSchema>();

  const [filter, setFilter] = useState<PaginationParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
  });

  const [warehouseId, setWarehouseId] = useState<string>(null);
  const [isGetInventories, setIsGetInventories] = useState<boolean>(false);

  const warehouseIdWatch = watch('warehouseId');
  const itemInventoriesWatch = watch('itemInventories');
  const statusWatch = watch('status');

  const { data: itemInventories, refetch } =
    useGetInventoriesStorageItemsAllByWarehouse(warehouseId, {
      page: DEFAULT_PAGE,
      size: DEFAULT_PAGE_SIZE_NAX,
    });

  const disabled =
    statusWatch === (STOCKTAKING_STATUS.WAITING_APPROVED as string) ||
    statusWatch === (STOCKTAKING_STATUS.CANCELLED as string) ||
    statusWatch === (STOCKTAKING_STATUS.APPROVED as string) ||
    statusWatch === (STOCKTAKING_STATUS.COMPLETED as string);

  const handlePageChange = (page: number) => {
    setFilter({ ...filter, page: page });
  };

  const handlePageSizeChange = (pageSize: number) => {
    setFilter({ ...filter, page: DEFAULT_PAGE, size: pageSize });
  };

  const columns = [
    {
      header: { render: `STT` },
      body: { render: ({ index }) => index + 1 },
    },
    {
      header: { render: `Mã hàng hóa` },
      body: { render: ({ data }) => data?.itemCode },
    },
    {
      header: { render: 'Tên hàng hóa' },
      body: {
        render: ({ data }) => (
          <Tooltip label={data?.itemName} target={`itemName-${data?.itemId}`}>
            <EllipsisParagraph
              text={data?.itemName}
              width={200}
              id={`itemName-${data?.itemId}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'Đơn vị' },
      body: {
        render: ({ data }) => (
          <Tooltip label={data?.uomName} target={`uom-${data?.itemId}`}>
            <EllipsisParagraph
              text={data?.uomName}
              width={100}
              id={`uom-${data?.itemId}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'SL tồn sổ sách' },
      body: {
        render: ({ data }) => (
          <Tooltip
            label={convertCurrency(data?.totalQty, false)}
            target={`qty-${data?.itemId}`}
          >
            <EllipsisParagraph
              text={convertCurrency(data?.totalQty, false)}
              id={`qty-${data?.itemId}`}
              width={100}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'SL tồn thực tế' },
      body: {
        render: ({ index }) => (
          <FormInputV2
            key={`itemInventories.${
              index + filter.page * filter.size
            }.totalQtyActual`}
            control={control}
            name={`itemInventories.${
              index + filter.page * filter.size
            }.totalQtyActual`}
            placeholder="SL tồn thực tế"
            disabled={disabled}
          />
        ),
      },
    },
    {
      header: { render: 'SL tồn chênh lệch' },
      body: {
        render: ({ data, index }) => {
          const idx = index + filter.page * filter.size;
          const totalQtyActualW = watch(
            `itemInventories.${idx}.totalQtyActual`,
          );
          const qtyDiff =
            Number(data?.totalQty ?? 0) - Number(totalQtyActualW ?? 0);

          return (
            <Tooltip
              label={convertCurrency(qtyDiff, false)}
              target={`qty-${data?.itemId}`}
            >
              <EllipsisParagraph
                text={convertCurrency(qtyDiff, false)}
                id={`qty-${data?.itemId}`}
                width={100}
              />
            </Tooltip>
          );
        },
      },
    },
    {
      header: { render: 'Ghi chú' },
      body: {
        render: ({ index }) => (
          <FormInputV2
            key={`itemInventories.${index + filter.page * filter.size}.note`}
            control={control}
            name={`itemInventories.${index + filter.page * filter.size}.note`}
            placeholder="Nhập ghi chú"
            disabled={disabled}
          />
        ),
      },
    },
  ];

  useEffect(() => {
    setIsGetInventories(false);
    setWarehouseId(warehouseIdWatch);
  }, [warehouseIdWatch]);

  useEffect(() => {
    if (itemInventories?.data?.length) {
      if (itemInventoriesWatch?.length && isGetInventories) {
        const items = itemInventoriesWatch?.map(x => {
          const selected = itemInventories?.data?.find(
            e => e.itemId === x.itemId,
          );
          if (selected)
            return { ...x, totalQty: `${selected.totalQuantity ?? 0}` };
          else return { ...x };
        });
        setValue('itemInventories', items);
        setIsGetInventories(false);
      } else {
        const items = itemInventories?.data?.map(x => ({
          itemId: x.itemId,
          itemCode: x.itemCode,
          itemName: x.itemName,
          uomName: x?.uomName,
          totalQty: `${x.totalQuantity ?? 0}`,
        }));
        setValue('itemInventories', items);
      }
    } else {
      setValue('itemInventories', []);
      setIsGetInventories(false);
    }
  }, [itemInventories]);

  const totalQty = itemInventoriesWatch?.reduce(
    (acc, item) => (acc += Number(item?.totalQty ?? 0)),
    0,
  );
  const totalQtyActual = itemInventoriesWatch?.reduce(
    (acc, item) => (acc += Number(item?.totalQtyActual ?? 0)),
    0,
  );

  return (
    <Flex direction="column" gap={16}>
      <Flex align="center" gap={16}>
        <Typography level={5} style={{ marginBottom: 0 }}>
          Hàng hóa
        </Typography>

        <ButtonV2
          variant="fill"
          color="blue"
          disabled={disabled}
          onClick={() => {
            refetch();
            setIsGetInventories(true);
          }}
        >
          Lấy tồn
        </ButtonV2>
      </Flex>

      <TablePagination
        table_id="items"
        columns={columns}
        data={
          JSON.parse(JSON.stringify(itemInventoriesWatch ?? []))?.splice(
            filter?.page * filter?.size,
            filter?.size,
          ) || []
        }
        total_pages={itemInventoriesWatch?.length || 0}
        itemsPerPage={filter?.size}
        handlePageClick={handlePageChange}
        handlePageSizeChange={handlePageSizeChange}
        custom_body_row={() => (
          <tr>
            <td colSpan={2} />
            <td colSpan={2}>Tổng tiền</td>
            <td>{convertCurrency(totalQty)}</td>
            <td>{convertCurrency(totalQtyActual)}</td>
            <td>{convertCurrency(totalQty - totalQtyActual)}</td>
            <td />
          </tr>
        )}
      />
    </Flex>
  );
};
