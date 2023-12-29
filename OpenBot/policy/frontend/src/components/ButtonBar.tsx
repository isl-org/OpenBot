import React from 'react';
import './ButtonBar.css'

export function ButtonBar(props: React.DetailedHTMLProps<React.HTMLAttributes<HTMLDivElement>, HTMLDivElement>) {
    return (
        <div className="button-bar" {...props}/>
    );
}
