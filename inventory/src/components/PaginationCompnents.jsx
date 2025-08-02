import React from 'react'

const PaginationComponents = ({ currentPage, totalPage, onPageChange }) => {
    const pageNumbers = Array.from({ length: totalPage }, (_, i) => i + 1);

    return (
        <div className='pagination-container'>
            <button
                className='pagination-button'
                disabled={currentPage === 1}
                onClick={() => onPageChange(currentPage - 1)}
            >
                &laquo; Prev
            </button>    

            {pageNumbers.map((number) => (
                <button
                    key={number}
                    className={`pagination-button ${currentPage === number ? "active" : ""}`}
                    onClick={() => onPageChange(number)}
                >
                    {number}
                </button>
            ))}

            <button
                className='pagination-button'
                disabled={currentPage === totalPage}
                onClick={() => onPageChange(currentPage + 1)}
            >
                Next &raquo;
            </button>
        </div>
    )
}

export default PaginationComponents
