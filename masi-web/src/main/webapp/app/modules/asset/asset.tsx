
import { ICON_PATH } from 'app/constants/common';

import { useQuery } from '@tanstack/react-query';
import ButtonFilter from 'app/components/ButtonV2/ButtonFilter';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import DownloadExcelBtn from 'app/components/ButtonV2/DownloadExcelBtn';
import ModalWrapper from 'app/components/ButtonV2/ModalWrapper';
import CardV2 from 'app/components/CardV2/CardV2';
import BadgeV2 from 'app/components/badge/badge-v2';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import Flex from 'app/components/flex/flex';
import InputSearch from 'app/components/input/input-search';
import { TableColumns } from 'app/components/table-v2/Table';
import TablePagination from 'app/components/table-v2/TablePagination';
import Tooltip from 'app/components/tooltip/tooltip';
import { Typography } from 'app/components/typography/typography';
import GroupCode from 'app/components/wrap-select/GroupCode';
import { PATH } from 'app/constants/path';
import useGoTo from 'app/hooks/use-go-to';
import { MapKeySelect } from 'app/shared/model/arr-obj.model';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import axios from 'axios';
import { keyBy } from 'lodash';
import { assetApis } from './apis/api';
import { useAssetList } from './apis/api.hook';
import './asset.scss';
import AssetFilter from './component/modals/asset-filter';
import { statusSelectData } from './constants/status';
import { Asset } from './types/list';
import AuthGuard from 'app/components/guards/auth-guard';


function Asset() {
    const { goTo } = useGoTo()

    const {
      assetList,
      handleSearch,
      handleQuery,
      query
    } = useAssetList()

    const groupCode: MapKeySelect = useQuery({
      queryKey: ['itemCategory-groupCode'],
      queryFn: () => axios.get<PaginationResponse<GroupCode>>('/services/masilogistics/api/item-categories'),
      select: res => keyBy(res?.data?.data, 'code')
    });

    const columns: TableColumns<Asset> = [
      {
        header: {
          render: 'Mã TS',
        },
        body: {
          render: ({ data }) => (
            <Tooltip label={'MTS_N01'} target={`code-${data?.id}`}>
              <EllipsisParagraph
                text={data?.code}
                width={200}
                id={`code-${data?.id}`}
                className="attachment-link"
                onClick={goTo(PATH.ASSET_CREATE + '/' + data?.id)}
              />
            </Tooltip>
          ),
        },
      },
      {
        header: {
          render: 'Mã từ kho',
        },
        body: {
          render: ({ data }) => (
            <Tooltip label={data?.warehouse?.code} target={`code-${data?.id}`}>
              <EllipsisParagraph
                text={data?.warehouse?.code}
                width={200}
                id={`code-${data?.id}`}
              />
            </Tooltip>
          ),
        },
      },
      {
        header: {
          render: 'Diễn giải',
        },
        body: {
          render: ({ data }) => (
            <Tooltip label={data?.notes} target={`name-${data?.id}`}>
              <EllipsisParagraph
                text={data?.notes}
                width={200}
                id={`name-${data?.id}`}
              />
            </Tooltip>
          ),
        },
      },
      {
        header: {
          render: 'Phân nhóm',
        },
        body: {
          render: ({ data }) => (
            <Tooltip label={groupCode?.data?.[data?.item?.itemCategory?.code]?.name} target={`address-${data?.id}`}>
              <EllipsisParagraph
                text={groupCode?.data?.[data?.item?.itemCategory?.code]?.name}
                width={200}
                id={`address-${data?.id}`}
              />
            </Tooltip>
          ),
        },
      },
      {
        header: {
          render: 'Lý do nhập',
        },
        body: {
          render: ({ data }) => (
            <Tooltip label={data?.itemInfo?.attribute?.reasonForEnteringAsset} target={`representative-${data?.id}`}>
              <EllipsisParagraph
                text={data?.itemInfo?.attribute?.reasonForEnteringAsset}
                width={200}
                id={`representative-${data?.id}`}
              />
            </Tooltip>
          ),
        },
      },
      {
        header: {
          render: 'Nguyên giá',
        },
        body: {
          render: ({ data }) => (
            <Tooltip label={String(data?.price)} target={`phone-${data?.id}`}>
              <EllipsisParagraph
                text={data?.price}
                width={200}
                id={`phone-${data?.id}`}
              />
            </Tooltip>
          ),
        },
      },
      {
        header: {
          render: 'GT còn lại',
        },
        body: {
          render: ({ data }) => (
            <Tooltip label={String(data?.remainingPrice)} target={`email-${data?.id}`}>
              <EllipsisParagraph
                text={data?.remainingPrice}
                width={200}
                id={`email-${data?.id}`}
              />
            </Tooltip>
          ),
        },
      },
      {
        header: {
          render: 'SL còn lại',
        },
        body: {
          render: ({ data }) => (
            <Tooltip
              label={String(data?.quantity)}
              target={`paymentTermNumber-${data?.id}`}
            >
              <EllipsisParagraph
                text={data?.quantity}
                width={200}
                id={`paymentTermNumber-${data?.id}`}
                onClick={() => {}}
              />
            </Tooltip>
          ),
        },
      },
      {
        header: {
          render: 'Trạng thái',
        },
        body: {
          render: ({ data }) => (
            <BadgeV2 color={'success'}>
              {statusSelectData?.obj?.[data?.status]?.label}
            </BadgeV2>
          ), 
        },
      },
      {
        header: { render: '' },
        body: {
          render: ({ data }) => (
            <Flex align="center">
              <ButtonV2
                variant="text"
                isBoxShadow={false}
                onClick={goTo(PATH.ASSET_CREATE + '/' + data?.id)}
              >
                <img src={ICON_PATH + 'edit-3.svg'} alt="edit" />
              </ButtonV2>
            </Flex>
          ),
        },
      },
    ];

    return (
      <div className="page_container page_container-V2">
        <CardV2
          header={
            <Flex justify="space-between" align="center">
              <Typography level={4}>Tài sản</Typography>
              {/* <ButtonAdd onClick={goTo(PATH.ASSET_CREATE)} text='Thêm' /> */}
            </Flex>
          }
        >
          <Flex
            className="rp__header"
            justify="space-between"
            align="center"
            style={{ marginBottom: 12 }}
          >
            <InputSearch onChange={handleSearch('search')} />
            <Flex gap={16}>
              <ModalWrapper
                renderTarget={({ onToggle }) => (
                  <ButtonFilter onClick={onToggle} />
                )}
                renderModal={() => <AssetFilter />}
              />
              <AuthGuard permissionKey="ASSET.EXPORT">
                <DownloadExcelBtn
                  axiosFn={assetApis.exportExcel}
                  fileName="TaiSan"
                />
              </AuthGuard>
            </Flex>
          </Flex>
          <TablePagination<Asset>
            table_id="asset-table"
            columns={columns}
            data={assetList?.data?.data}
            total_pages={assetList?.data?.totalRecord}
            itemsPerPage={query?.size}
            handlePageClick={handleQuery('page')}
            handlePageSizeChange={handleQuery('size')}
            isLoading={assetList?.isLoading}
          />
        </CardV2>
      </div>
    );
}

export default Asset