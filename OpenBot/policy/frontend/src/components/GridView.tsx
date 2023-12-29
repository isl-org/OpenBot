import React from 'react';
import './GridView.css'

export function GridView(props: React.DetailedHTMLProps<React.HTMLAttributes<HTMLDivElement>, HTMLDivElement>) {
    return (
        <div className="grid-view" {...props}/>
    );
}
