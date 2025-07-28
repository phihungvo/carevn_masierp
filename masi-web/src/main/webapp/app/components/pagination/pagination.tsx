import './pagination.scss';
import { usePagination, DOTS } from 'app/hooks/use-pagination';
import React, { useEffect, useState } from 'react';
import InputNumber from '../input/input-number';

interface IPagination {
  onPageChange?: (page: number, size: number) => void;
  totalCount: number;
  siblingCount?: number;
  page: number;
  size: number;
  className: string;
  showJumper?: boolean;
  showTotal?: boolean;
  totalLabel?: string;
}

const Pagination = (props: IPagination) => {
  const {
    onPageChange,
    totalCount,
    siblingCount = 1,
    page,
    size,
    className,
    showJumper = true,
    showTotal = false,
    totalLabel = 'Tổng số',
  } = props;

  const [curPage, setCurPage] = useState(page + 1);
  const [pageSize, setPageSize] = useState(size);

  const paginationRange = usePagination({
    page: curPage,
    totalCount,
    siblingCount,
    size,
  });

  // if (!paginationRange || paginationRange.length < 2) {
  //   return null;
  // }

  const onNext = () => {
    onPageChange && onPageChange(page + 1, size);
  };

  const onPrevious = () => {
    onPageChange && onPageChange(page - 1, size);
  };

  const onChangePageSize = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;

    setPageSize(Number(value));
  };

  const onChangePage = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = Number(e.target.value);
    const lastPage = Number(paginationRange?.[paginationRange?.length - 1]);

    if (!paginationRange?.length) {
      setCurPage(1);
      return;
    }

    if (value >= lastPage) {
      setCurPage(lastPage);
      return;
    }

    setCurPage(value);
  };

  const handleChangePageSize = () => {
    if (!pageSize) {
      setPageSize(1);
    }
    onPageChange && onPageChange(0, !pageSize ? pageSize + 1 : pageSize);
  };

  const handleChangePage = () => {
    if (!curPage) {
      setCurPage(1);
    }
    onPageChange && onPageChange(!curPage ? 0 : Number(curPage) - 1, size);
  };

  let lastPage = paginationRange[paginationRange.length - 1];

  useEffect(() => {
    setCurPage(page + 1);
  }, [page]);

  const disabledPrev = page === 0 || !totalCount || totalCount <= size;
  const disabledNext = curPage === lastPage || !totalCount || totalCount <= size;

  return (
    <div className="pagination">
      {showJumper && (
        <div className="pagination-jumper">
          Hàng trên mỗi trang{' '}
          <InputNumber
            value={pageSize === 0 ? '' : pageSize}
            className="num-row"
            type="number"
            min={0}
            onChange={onChangePageSize}
            onBlur={handleChangePageSize}
          />{' '}
          trang{' '}
          <InputNumber
            value={curPage === 0 ? '' : curPage}
            onChange={onChangePage}
            className="page"
            type="number"
            min={0}
            onBlur={handleChangePage}
          />
          {showTotal && (
            <div className="mt-1 fw-bold">
              {totalLabel}: {totalCount}
            </div>
          )}
        </div>
      )}

      <ul className={`pagination-container ${className ?? ''})`}>
        <li
          className={`pagination-item pagination-item-left-arrow ${disabledPrev ? 'disabled' : ''}`}
          {...(!disabledPrev && { onClick: onPrevious })}
        >
          {disabledPrev ? (
            <img className="arrow" src="content/images/vuesax/linear/arrow-left.svg" alt="arrow-left" />
          ) : (
            <img className="arrow" src="content/images/vuesax/linear/arrow-right.svg" alt="arrow-right" style={{ rotate: '-180deg' }} />
          )}
        </li>
        {paginationRange.map((pageNumber, index) => {
          const pageNumberIdx = Number(pageNumber) - 1;

          if (pageNumber === DOTS) {
            return (
              <li key={`${pageNumber}${index}`} className="pagination-item dots">
                &#8230;
              </li>
            );
          }

          return (
            <li
              key={pageNumber}
              className={`pagination-item  ${pageNumberIdx === page || !totalCount ? 'selected' : ''}`}
              onClick={() => onPageChange(pageNumberIdx, size)}
            >
              {pageNumber}
            </li>
          );
        })}
        <li className={`pagination-item ${disabledNext ? 'disabled' : ''}`} {...(!disabledNext && { onClick: onNext })}>
          {disabledNext ? (
            <img className="arrow" src="content/images/vuesax/linear/arrow-left.svg" alt="arrow-left" style={{ rotate: '180deg' }} />
          ) : (
            <img className="arrow" src="content/images/vuesax/linear/arrow-right.svg" alt="arrow-right" />
          )}
        </li>
      </ul>
    </div>
  );
};

export default Pagination;
