import React from 'react';
import ReactPaginate from 'react-paginate';
import './PaginationV2.scss';
import '../pagination/pagination.scss';

type Props = {
  total_pages: number;
  itemsPerPage?: number;
  handlePageClick?: (selected_page: number) => void;
  handlePageSizeChange?: (pageSize: number) => void;
};

const PaginationV2 = (props: Props) => {
  const { itemsPerPage, handlePageClick, total_pages } = props;

  return (
    <>
      <ReactPaginate
        previousLabel={
          <div className="first-last">
            <img
              src="content/images/vuesax/linear/previous-page.svg"
              alt="previous-page"
            />
            <span>Trước</span>
          </div>
        }
        nextLabel={
          <div className="first-last">
            <span>Sau</span>
            <img
              src="content/images/vuesax/linear/next-page.svg"
              alt="previous-page"
            />
          </div>
        }
        onPageChange={
          handlePageClick && (({ selected }) => handlePageClick(selected))
        }
        pageRangeDisplayed={3}
        marginPagesDisplayed={2}
        pageCount={Math.ceil(total_pages / itemsPerPage)}
        pageClassName="page-item"
        pageLinkClassName="page-link"
        previousClassName="page-item"
        previousLinkClassName="page-link"
        nextClassName="page-item"
        nextLinkClassName="page-link"
        breakLabel="..."
        breakClassName="page-item"
        breakLinkClassName="page-link"
        containerClassName="pagination pagination-v2"
        activeClassName="active"
        renderOnZeroPageCount={null}
      />
    </>
  );
};

PaginationV2.defaultProps = {
  itemsPerPage: 4,
};

export default PaginationV2;
